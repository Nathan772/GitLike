export type ContributionColors = {
    BUILD: string;
    CODE: string;
    RESOURCE: string;
    CONFIG: string;
    DOCUMENTATION: string;
    OTHER: string;
    COMMENTS: string;
};

const colors: ContributionColors = {
    "BUILD": "#FFDD4A",
    "CODE": "#FE9000",
    "RESOURCE": "#5ADBFF",
    "CONFIG": "#FE654F",
    "DOCUMENTATION": "#D81159",
    "OTHER": "#5F0A87",
    "COMMENTS": "#A89F9F",
}

const hoverColors: ContributionColors = {
    "BUILD": "#F5C800",
    "CODE": "#CC7400",
    "RESOURCE": "#00C0F5",
    "CONFIG": "#FE3D20",
    "DOCUMENTATION": "#BD0F4F",
    "OTHER": "#500972",
    "COMMENTS": "#817474",
}

export { colors, hoverColors };