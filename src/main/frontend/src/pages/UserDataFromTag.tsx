import { createSignal, onMount } from 'solid-js';
import { useNavigate, useSearchParams } from '@solidjs/router';
import {getUserContributionByTag  } from "../utils";
import { Modal } from 'bootstrap';
import { JSONResponse, TabContribution, ContributionIntermediate } from '../types/JSONResponse';
import { setStoreError } from '../store';

function UserDataFromTag() {
    const navigate = useNavigate();
    /* the different set refers to the method you will
    use to set the variable associated to the set*/
    //const [repositoryName, setRepository] = createSignal('');
    /* this array will contains the list of users for this tag*/
    const [contributorName, setContributorName] = createSignal('');
    /* this array will contains the list of users for this tag*/
    const [contributorEmail, setContributorEmail] = createSignal('');

    const [contributions, setContributions] = createSignal([] as ContributionIntermediate[]);

    const [tagName, setTagName] = createSignal('');
    onMount(async () => {
        const modal = new Modal(document.getElementById('modal')!);
        const modalElement = document.getElementById('modal')!;
        const [searchParams] = useSearchParams();
        //retrieve the value associated to tag="" in the web page link
        const tagUsed = searchParams.tag;
        const contributorEmail = searchParams.contributorEmail;
        const contributorName = searchParams.contributorName;
         if (!tagUsed) {
                    console.log(`le mail qui pose problème contient ${contributorEmail}`);
                    console.log("test nathan2 != tagUsed mais entrée dans User.tsx")
                    navigate('/not-found');
                    return;
         }

         console.log(`le mail contient ${contributorEmail}`);

        //const tag = searchParams.url;
        modalElement.addEventListener('shown.bs.modal', async () => {
            const res = await getUserContributionByTag (tagUsed, contributorName,contributorEmail) as JSONResponse;
            const data = res.data as TabContribution;
            modal.hide();
            if (res.status === "success") {
            //JSONContributors(List<String> Contributors, String tagName, String repositoryName)
                //give to the variable contributors the data from the field contributors
                setContributions(data.contributions);
                setTagName(tagUsed);
                /*retrieve the email from the contributor*/
                setContributorEmail(data.contributorEmail);
                /*retrieve the name from the contributor*/
                setContributorName(data.contributorName);
                console.log(`Les contributions contiennent : ${data.contributions}`);


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


                <p> Tag : {tagName()}  </p>

                <div class="mt-5">
                    <h2 class="h3">Les contributions du contributeur {contributorName()} as {contributorEmail()}</h2>

                    <ul>

                        {contributions() && contributions().map(contribution=>
                                <li class="list-unstyled">
                                    Le type de fichier : {contribution.type}
                                    <ul>
                                          <li>L'extension : {contribution.extension}</li>
                                          <li>Les lignes de code : {contribution.lines}</li>

                                          <li>Les lignes de commentaire : {contribution.commentLines}</li>
                                          <li>Le total des lignes : {contribution.commentLines+contribution.lines}</li>
                                    </ul>

                                    <br/>

                                </li>


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

export default UserDataFromTag