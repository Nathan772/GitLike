import { For } from "solid-js";
import { Contributions } from "../types/JSONResponse";

import BarChart from "./BarChart";
import RadarChart from "./RadarChart";

function Stats(props: { contributions: Contributions, showComments?: boolean }) {

    return (
        <>
            <BarChart contributions={props.contributions} showComments={props.showComments} />
            <div class="container text-center">
                <div class="row row-cols-2 justify-content-center">
                    <For each={props.contributions.contributions}>
                        {(contribution) => (
                            <RadarChart contributionsData={contribution} showComments={props.showComments} />
                        )}
                    </For>
                </div>
            </div>

            {/* <For each={props.contributions.contributions}>
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
            </For> */}
        </>
    );
}

export default Stats;
