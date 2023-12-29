import { onMount, createEffect, createSignal } from "solid-js";
import { Contributions, ContributionsData } from "../types/JSONResponse";
import { Chart } from "chart.js/auto";

function BarChart(props: { contributions: Contributions, showComments?: boolean }) {
    const [containerHeight, setContainerHeight] = createSignal(0);

    let chart: Chart;

    type ContributionColors = {
        BUILD: string;
        CODE: string;
        RESOURCE: string;
        CONFIG: string;
        DOCUMENTATION: string;
        OTHER: string;
        COMMENTS: string;
    };

    const colors: ContributionColors = {
        "BUILD": "#FFDD4A",
        "CODE": "#FE9000",
        "RESOURCE": "#5ADBFF",
        "CONFIG": "#FE654F",
        "DOCUMENTATION": "#D81159",
        "OTHER": "#5F0A87",
        "COMMENTS": "#A89F9F",
    }

    const hoverColors: ContributionColors = {
        "BUILD": "#F5C800",
        "CODE": "#CC7400",
        "RESOURCE": "#00C0F5",
        "CONFIG": "#FE3D20",
        "DOCUMENTATION": "#BD0F4F",
        "OTHER": "#500972",
        "COMMENTS": "#817474",
    }

    const updateChart = () => {
        const barThickness = 50;

        const ctx = document.getElementById("bar-chart-" + props.contributions.tagName) as HTMLCanvasElement;
        const labels = props.contributions.contributions.map((contribution: ContributionsData) => contribution.author);

        const datasetsLabels = props.contributions.contributions.flatMap((contribution: ContributionsData) =>
            Object.keys(contribution.contributionsByType)
        ).filter((value, index, self) => self.indexOf(value) === index);

        if (props.showComments) {
            datasetsLabels.push("CODE COMMENTS");
        }

        const datasetsData = datasetsLabels.map((label) => {
            return props.contributions.contributions.map((contribution: ContributionsData) => {
                let total = contribution.contributionsByType[label]?.total || 0;
                let commentCount = contribution.contributionsByType[label]?.totalComment || 0;
                if (props.showComments) {
                    if (label === "CODE COMMENTS") {
                        total = contribution.contributionsByType["CODE"]?.totalComment || 0;
                    } else if (label === "CODE") {
                        total -= commentCount;
                    }
                }
                return total;
            });
        });

        const datasets = datasetsLabels.map((label, index) => {
            return {
                label: label,
                data: datasetsData[index],
                barThickness: barThickness,
                backgroundColor: colors[label as keyof ContributionColors],
                hoverBackgroundColor: hoverColors[label as keyof ContributionColors],
            }
        });

        console.log(datasets);

        const data = {
            labels: labels,
            datasets: datasets
        };

        if (chart) {
            chart.destroy();
        }

        const height = barThickness * labels.length + 200;
        setContainerHeight(height);

        chart = new Chart(ctx, {
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
