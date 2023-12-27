import { For, onMount, createEffect } from "solid-js";
import { Contributions, ContributionsData, Contribution } from "../types/JSONResponse";
import { Chart } from "chart.js/auto";

function Stats(props: { contributions: Contributions }) {
    let chart: Chart;

    type ContributionColors = {
        BUILD: string;
        CODE: string;
        RESOURCE: string;
        CONFIG: string;
        DOCUMENTATION: string;
        OTHER: string;
    };

    const colors: ContributionColors = {
        "BUILD": "#FFDD4A",
        "CODE": "#FE9000",
        "RESOURCE": "#5ADBFF",
        "CONFIG": "#FE654F",
        "DOCUMENTATION": "#D81159",
        "OTHER": "#5F0A87",
    }

    const hoverColors: ContributionColors = {
        "BUILD": "#F5C800",
        "CODE": "#CC7400",
        "RESOURCE": "#00C0F5",
        "CONFIG": "#FE3D20",
        "DOCUMENTATION": "#BD0F4F",
        "OTHER": "#500972",
    }

    const updateChart = () => {
        const ctx = document.getElementById("chart-" + props.contributions.tagName) as HTMLCanvasElement;

        const labels = props.contributions.contributions.map((contribution: ContributionsData) => contribution.author);
        console.log("labels", labels);

        const datasetsLabels = Object.keys(props.contributions.contributions[0].contributionsByType);
        const datasetsData = datasetsLabels.map((label) => {
            return props.contributions.contributions.map((contribution: ContributionsData) => {
                return contribution.contributionsByType[label]?.total || 0;
            });
        });

        const datasets = datasetsLabels.map((label, index) => {
            return {
                label: label,
                data: datasetsData[index],
                barThickness: 50,
                maxBarThickness: 100,
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

        chart = new Chart(ctx, {
            type: 'bar',
            data: data,
            options: {
                animation: false,
                scales: {
                    x: {
                        stacked: true
                    },
                    y: {
                        beginAtZero: true,
                        stacked: true
                    }
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
            <canvas id={"chart-" + props.contributions.tagName}></canvas>
            <For each={props.contributions.contributions}>
                {(contribution: ContributionsData) => (
                    <>
                        <p>{contribution.author}</p>
                        <For each={Object.entries(contribution.contributionsByType)}>
                            {([contributionType, contributionDetails]: [string, Contribution]) => (
                                <>
                                    <h4>{contributionType}</h4>
                                    <p>Total: {contributionDetails.total}</p>
                                    <For each={Object.entries(contributionDetails.details)}>
                                        {([language, languageCount]) => (
                                            <div>
                                                <span>{language}: </span>
                                                <span>Count: {languageCount.count}</span>
                                                <span>, Comment Count: {languageCount.commentCount}</span>
                                            </div>
                                        )}
                                    </For>
                                </>
                            )}
                        </For>
                    </>
                )}
            </For>
        </>
    );
}

export default Stats;
