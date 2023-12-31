import { onMount, createEffect, createSignal } from "solid-js";
import { Contributions } from "../../types/JSONResponse";
import { Chart } from "chart.js/auto";
import { colors, hoverColors } from "../../types/ChartColors";
import { ContributionColors } from "../../types/ChartColors";

function BarChart(props: { contributions: Contributions, showComments?: boolean }) {
    const [containerHeight, setContainerHeight] = createSignal(0);

    let chartInstance: Chart;
    const barThickness = 50;

    const TYPE_CODE = "CODE";
    const TYPE_CODE_COMMENTS = "CODE COMMENTS";

    const generateChartData = () => {
        const labels = props.contributions.contributions.map(c => c.author);

        let datasetsLabels = props.contributions.contributions.flatMap(c => Object.keys(c.contributionsByType))
            .filter((value, index, self) => self.indexOf(value) === index);

        if (props.showComments) {
            datasetsLabels.push(TYPE_CODE_COMMENTS);
        }

        const datasetsData = datasetsLabels.map(label => {
            return props.contributions.contributions.map(c => {
                if (props.showComments && label === TYPE_CODE_COMMENTS) {
                    return c.contributionsByType[TYPE_CODE]?.totalComment || 0;
                }

                let total = c.contributionsByType[label]?.total || 0;
                if (props.showComments && label === TYPE_CODE) {
                    total -= c.contributionsByType[TYPE_CODE]?.totalComment || 0;
                }
                return total;
            });
        });

        const datasets = datasetsLabels.map((label, index) => ({
            label: label,
            data: datasetsData[index],
            barThickness: barThickness,
            backgroundColor: colors[label as keyof ContributionColors],
            hoverBackgroundColor: hoverColors[label as keyof ContributionColors],
        }));

        return { labels, datasets };
    };

    const updateChart = () => {
        const ctx = document.getElementById("bar-chart-" + props.contributions.tagName) as HTMLCanvasElement;
        const { labels, datasets } = generateChartData();

        const data = {
            labels: labels,
            datasets: datasets
        };

        if (chartInstance) {
            chartInstance.destroy();
        }

        const height = barThickness * labels.length + 200;
        setContainerHeight(height);

        chartInstance = new Chart(ctx, {
            type: 'bar',
            data: data,
            options: {
                animation: false,
                scales: {
                    x: {
                        stacked: true,
                        position: 'top',
                    },
                    y: {
                        beginAtZero: true,
                        stacked: true,
                    }
                },
                responsive: true,
                maintainAspectRatio: false,
                indexAxis: 'y',
                plugins: {
                    legend: {
                        position: 'right',
                        align: 'start',
                        labels: {
                            boxWidth: 20,
                            boxHeight: 20,
                            padding: 20,
                            usePointStyle: true,
                        }
                    },
                    title: {
                        display: true,
                        text: 'Contributions by author (by lines of code)'
                    },
                }
            }
        });
    };

    onMount(updateChart);

    createEffect(() => {
        updateChart();
    });


    return (
        <>
            <div style="overflow-x: auto; max-height: 40vh">
                <div style={`height: ${containerHeight()}px; min-height: 40vh`}>
                    <canvas id={"bar-chart-" + props.contributions.tagName}></canvas>
                </div>
            </div>
        </>
    );
}

export default BarChart;
