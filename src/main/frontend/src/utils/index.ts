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
    return response.json();
}

export const getHistory = async () => {
    const response = await fetch(`${API_URL}/history`);
    return response.json();
}

export const deleteHistoryItem = async (repoURL: string) => {
    const response = await fetch(`${API_URL}/history`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ url: repoURL }),
    });
    return response.json();
}