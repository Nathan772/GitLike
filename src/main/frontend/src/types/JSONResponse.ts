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

export interface Contributions {
    tagName: string;
    contributions: ContributionsData[];
}

export interface ContributionsData {
    author: string;
    contributionsByType: ContributionsByType;
}

export type ContributionTypeKey = "BUILD" | "CODE" | "RESOURCE" | "CONFIG" | "DOCUMENTATION" | "OTHER";

export interface ContributionsByType {
    [key: string]: Contribution;
}

export interface Contribution {
    total: number;
    details: ContributionDetails;
}

export interface ContributionDetails {
    [key: string]: LanguageCount;
}

export interface LanguageCount {
    language: string;
    count: number;
    commentCount: number;
}