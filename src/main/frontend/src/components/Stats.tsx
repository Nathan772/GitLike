import { For } from "solid-js";
import { Contributions } from "../types/JSONResponse";

import BarChart from "./charts/BarChart";
import RadarChart from "./charts/RadarChart";

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
        </>
    );
}

export default Stats;
