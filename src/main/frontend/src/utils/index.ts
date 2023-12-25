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
    //System.out.println("le truc qui m'intrigue : "+`${API_URL}/repository-info?url=${repoURL}`gh);
    console.log(response);
    console.log("blablabla viki blabla")
    return response.json();
}