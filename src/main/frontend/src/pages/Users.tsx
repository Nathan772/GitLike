//import { For, createSignal, onMount } from 'solid-js';
import { createSignal, onMount } from 'solid-js';
import { useNavigate, useSearchParams } from '@solidjs/router';
//import { useNavigate } from '@solidjs/router';
import { getTagContributors } from "../utils";
import { Modal } from 'bootstrap';
import { JSONResponse, Contributor, ContributorsInfosForTag } from '../types/JSONResponse';
import { setStoreError } from '../store';
/*import { Toasts, addToast } from '../components/Toasts';
import { ToastInfo } from '../types/ToastInfo'; */
//import {useLocation} from "react-router-dom";

function UsersFromTagList() {
    const navigate = useNavigate();
    /* the different set refers to the method you will
    use to set the variable associated to the set*/
    const [repositoryName, setRepository] = createSignal('');
    /* this array will contains the list of users for this tag*/
    const [contributors, setContributors] = createSignal([] as Contributor[]);
    /* this variable will contains the tag chosen*/
    const [tagName, setTag] = createSignal('');
    const [urlUsed, setURL] = createSignal('');

    onMount(async () => {
        const modal = new Modal(document.getElementById('modal')!);
        const modalElement = document.getElementById('modal')!;
        const [searchParams] = useSearchParams();
        //retrieve the value associated to tag="" in the web page link
        const tagUsed = searchParams.tag;
        const urlUsedParam = searchParams.url;
         if (!tagUsed) {
                    console.log("test nathan2 != tagUsed mais entrée dans User.tsx")
                    navigate('/not-found');
                    return;
         }
        //const tag = searchParams.url;
        modalElement.addEventListener('shown.bs.modal', async () => {
            const res = await getTagContributors(tagUsed) as JSONResponse;
            const data = res.data as ContributorsInfosForTag;
            modal.hide();
            if (res.status === "success") {
            //JSONContributors(List<String> Contributors, String tagName, String repositoryName)
                //give to the variable contributors the data from the field contributors
                setContributors(data.contributors);
                //this name of field must be the same as the JSONContributors fields name
                setRepository(data.repositoryName);
                /* retrieve the tags from the project */
                setTag(data.tagName);
                setURL(urlUsedParam);


            } else {
                setStoreError({ message: res.message });
                navigate('/not-found');
            }
        });
        modal.show();

    });

    return (
        <>

            <main class="container mt-5">
                <div class="d-inline-flex gap-3 align-items-center">
                    <button class="icon-button btn btn-outline-secondary rounded-circle p-2" onClick={() => navigate('/')}>
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                        </svg>
                    </button>

                </div>



                <p> Projet : {repositoryName()}  </p>

                <button type="button" class="btn btn-primary" onClick={() => navigate("/")}>Back to Home</button>
                <br/>
                <button type="button" class="btn btn-primary" onClick={() => navigate(`/repository?url=${urlUsed()}`)}>Back to Tags </button>
                <br/>
                <div class="mt-5">
                    <h2 class="h3">Liste des contributeurs pour le tag {tagName()} </h2>
                    <ul class="d-flex overflow-x-auto ps-0">

                        {contributors() && contributors().map(contributor=>
                            <div class="userAndEmail">
                                <li class="list-unstyled">
                                <button type="button" class="btn btn-outline-secondary text-nowrap me-2 mb-2"
                                onClick={() => navigate(`/contributorInfos?tag=${tagName()}&contributorName=${contributor.name}&contributorEmail=${contributor.email}&url=${urlUsed()}`)}>
                                        {contributor.name}
                                </button>
                                  <span class="hiddenEmail"> {contributor.email} </span>
                                </li>
                            </div>
                        )}

                    </ul>
                </div>



            </main>

            <div class="modal fade" id="modal" data-bs-backdrop="static" data-bs-keyboard="false" aria-labelledby="modalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="modalLabel">Resolving Contributors</h1>
                        </div>
                        <div class="modal-body">
                            <div class="d-flex justify-content-center">
                                <div class="spinner-border" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default UsersFromTagList