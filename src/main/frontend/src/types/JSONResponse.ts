export interface JSONResponse {
    message: string;
    status: "success" | "error";
    data: RepositoryInfos | URL;
}

export interface RepositoryInfos {
    name: string;
    URL: string;
    tags: string[];
}

export interface URL {
    URL: string;
}