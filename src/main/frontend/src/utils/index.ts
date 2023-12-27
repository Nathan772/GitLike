const API_URL = 'http://localhost:8080/api';

export const checkRepository = async (repoURL: string) => {
    const response = await fetch(`${API_URL}/check-repository`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ url: repoURL }),
    });
    return response.json();
};

export const getRepoInfos = async (repoURL: string) => {
    const response = await fetch(`${API_URL}/repository-info?url=${repoURL}`);
    console.log(response);
    return response.json();
}

/* this method enables to give to the api the link with the tag */
export const getTagContributors = async (tagName: string) => {
    /* the name associated to the equal must be the same you use for the function used in the gitController
     method created */
    const response = await fetch(`${API_URL}/Contributors-fromTag?tagName=${tagName}`);
    console.log(response);
    console.log("test get tag Contributors for the tag "+tagName);
    return response.json();
}

export const getUserContributionByTag = async (tagName: string, contributorName:string, contributorEmail:string) => {
    /* the name associated to the equal must be the same you use for the function used in the gitController
     method created */
    const response = await fetch(`${API_URL}/Contributor-InfosFromTag?tagName=${tagName}&contributorName=${contributorName}&contributorEmail=${contributorEmail}`);
    console.log(response);
    console.log("test get user contribution for the tag : "+tagName);
    return response.json();
}