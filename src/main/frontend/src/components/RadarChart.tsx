import { onMount, createEffect } from "solid-js";
import { ContributionsData } from "../types/JSONResponse";
import { Chart } from "chart.js/auto";

function RadarChart(props: { contributionsData: ContributionsData, showComments?: boolean }) {
    let chart: Chart;

    const updateChart = () => {
        const author = props.contributionsData.author;

        const ctx = document.getElementById("radar-chart-" + props.contributionsData.author) as HTMLCanvasElement;

        const labels = Object.keys(props.contributionsData.contributionsByType)
            .flatMap((label) => Object.keys(props.contributionsData.contributionsByType[label].details))
            .filter((value, index, self) => self.indexOf(value) === index);

        const aggregatedData = labels.map((label) => {
            return Object.keys(props.contributionsData.contributionsByType)
                .reduce((sum, contributionLabel) => {
                    return sum + (props.contributionsData.contributionsByType[contributionLabel].details[label]?.count || 0);
                }, 0);
        });

        console.log("labels", labels);

        const data = {
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

        if (chart) {
            chart.destroy();
        }

        chart = new Chart(ctx, {
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
