import {useParams} from "@solidjs/router";



function Repo () {
    const id = useParams().id
    console.log(id)
    return (
        <div>
            <h1>Repo</h1>
        </div>
    )
}

export default Repo