# GitClout

## Description

GitClout is a web application that allows users to analyze tags from a public repository (Github or Gitlab for example) and display information about the contributors.

## Table of Contents

- [GitClout](#gitclout)
  - [Description](#description)
  - [Table of Contents](#table-of-contents)
  - [Installation](#installation)
  - [Usage](#usage)
    - [About the results](#about-the-results)

## Installation

The project is built with Maven, so you can import it to your IDE and run it from there. You can also run it from the command line with the following command:

```bash
mvn package
```

The jar file will be located in the target folder.

```bash
java -jar target/gitclout-0.1.jar
```

## Usage

The application is a web application, so you can access it from your browser at the following address:

```bash
http://localhost:8080
```

The application will ask you for a repository URL. You can use any public repository URL, for example:

```bash
https://gitlab.ow2.org/asm/asm.git
```

Once you have entered the URL, the application will start analyzing the repository. This may take a while depending on the size of the repository.

The app is entirely written reactive, so when a tag is analyzed, the result is displayed immediately. A progress bar is displayed at the top of the page to indicate the progress of the analysis.

Once the analysis is complete, the results are stored in a database. The next time you analyze the same repository, only the new tags will be analyzed.
You can see already analyzed repositories in the "History" section of the home page.

### About the results

The results are displayed in charts. You can see the number of lines for a selection of contribution types:

- CODE: Number of lines of code
- RESOURCE: Number of resource files (images, etc.) (**not lines**)
- CONFIG: Number of lines of configuration files (pom.xml, etc.)
- DOCUMENATION: Number of lines of documentation files (README.md, etc.)
- BUILD: Number of lines of build files (Dockerfile, etc.)

You can click "Show Comments" to see the number of comments for each CODE contribution type.
