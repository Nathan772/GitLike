import { onMount, createEffect } from "solid-js";
import { ContributionsData } from "../../types/JSONResponse";
import { Chart } from "chart.js/auto";

function RadarChart(props: { contributionsData: ContributionsData, showComments?: boolean }) {
    let chartInstance: Chart;

    const generateChartData = (author: string) => {
        const labels = Object.keys(props.contributionsData.contributionsByType)
            .flatMap((label) => Object.keys(props.contributionsData.contributionsByType[label].details))
            .filter((value, index, self) => self.indexOf(value) === index);

        if (props.showComments) {
            const labelsComments = Object.keys(props.contributionsData.contributionsByType)
                .filter(label => label === "CODE")
                .flatMap((label) => Object.keys(props.contributionsData.contributionsByType[label].details))
                .filter((value, index, self) => self.indexOf(value) === index)
                .map((label) => label + " Comments");

            labels.push(...labelsComments);
        }

        const aggregatedData = labels.map((label) => {
            if (label.endsWith(" Comments")) {
                const codeLabel = label.substring(0, label.length - " Comments".length);
                return Object.keys(props.contributionsData.contributionsByType)
                    .reduce((sum, contributionLabel) => {
                        return sum + (props.contributionsData.contributionsByType[contributionLabel].details[codeLabel]?.commentCount || 0);
                    }, 0);
            }
            return Object.keys(props.contributionsData.contributionsByType)
                .reduce((sum, contributionLabel) => {
                    return sum + (props.contributionsData.contributionsByType[contributionLabel].details[label]?.count || 0);
                }, 0);
        });

        return {
            labels: labels,
            datasets: [{
                label: `${author} contributions`,
                data: aggregatedData,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgb(54, 162, 235)',
                pointBackgroundColor: 'rgb(54, 162, 235)',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: 'rgb(54, 162, 235)',
                fill: true,
            }]
        };
    };

    const updateChart = () => {
        const author = props.contributionsData.author;
        const data = generateChartData(author);

        if (chartInstance) {
            chartInstance.destroy();
        }
        const ctx = document.getElementById("radar-chart-" + props.contributionsData.author) as HTMLCanvasElement;

        chartInstance = new Chart(ctx, {
            type: 'radar',
            data: data,
            options: {
                animation: false,
                plugins: {
                    title: {
                        display: true,
                        text: `Contributions by ${author} (by lines of code)`
                    },
                },
                elements: {
                    line: {
                        borderWidth: 1
                    }
                }
            },
        });

    };

    onMount(updateChart);

    createEffect(() => {
        updateChart();
    });


    return (
        <>
            <div style="width: 50%">
                <canvas id={"radar-chart-" + props.contributionsData.author}></canvas>
            </div>
        </>
    );
}

export default RadarChart;
