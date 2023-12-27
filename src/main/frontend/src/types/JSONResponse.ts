export interface JSONResponse {
    message: string;
    status: "success" | "error";
    data: RepositoryInfos | URL | ContributorsInfosForTag | Contributor | ContributionIntermediate  | TabContribution ;
}

export interface RepositoryInfos {
    name: string;
    URL: string;
    tags: string[];
}

/* this structure retrieve several Contributors data */
/* la question est-ce que le contributorInfo ici pourra accepter un type
contributorInfo défini dans java dans JSONContributors ?*/
export interface ContributorsInfosForTag {
    contributors:Contributor[];
    tagName: string;
    repositoryName: string;
}

/* this structure represents a single contributor data*/

export interface Contributor {
    name: string;
    email: string;
}

export interface TabContribution {
    contributions:ContributionIntermediate[];
    contributorEmail:string;
    contributorName:string;
    tagName:string;
}

/* this structure rerpresents the entire contribution from a user
for a specific kind of langage/file */

export interface ContributionIntermediate {
    contributorName:string;
    contributorEmail:string;
    type:string;
    extension:string;
    programmingLanguage:boolean;
    lines:number;
    commentLines:number;
}

export interface URL {
    URL: string;
}