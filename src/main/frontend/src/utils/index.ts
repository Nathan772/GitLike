const API_URL = 'http://localhost:8080/api';

export const postRepository = async (repoURL: string) => {
    console.log(repoURL);
    const response = await fetch(`${API_URL}/repository`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ url: repoURL }),
    });

    console.log(response);

    return response.json();
};