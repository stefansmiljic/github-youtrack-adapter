# README

## How to run the program

This program was written in IntelliJ IDEA, in Kotlin programming language.
Steps you should take:

- Run Gradle, and Load Gradle projects
- Create Github Repository and create some issues for it
- Create Github token and save it (you are not required to enter it in the project, it is used as environment variable)
- Create Youtrack account, and create Youtrack token
- Create Youtrack project
- In the IDEA, you need to insert the following environment variables:
    - GITHUB_REPO=`<github_username>`/`<repository-name>`  //do not add slashes (/) at the end
    - YOUTRACK_URL=https://`<your-youtrack>`.youtrack.cloud
    - GITHUB_TOKEN=`<your-token>`
    - YOUTRACK_TOKEN=`<your-token>`
- Main class is called MainKt, I was using java ms-21
- When you run the program, it will show the IDs of your current projects, you are required to insert the ID in order to proceed
- After that, just let the program finish, it will import all issues from the Github repo you inserted into your YouTrack project.

### Console output of the program after updating 4 existing issues and adding 1 new
<img width="1160" height="376" alt="image" src="https://github.com/user-attachments/assets/f2cbd1b1-7af7-4f81-ab72-1b29fe0db927" />

### Here you can see the issues on GitHub as they are made in YouTrack
<img width="399" height="291" alt="image" src="https://github.com/user-attachments/assets/2ef14e82-72ce-4034-a142-b4eea7185a71" />
<img width="469" height="290" alt="image" src="https://github.com/user-attachments/assets/248e42fc-4658-48fb-913e-f3862fa57fba" />
<img width="1471" height="255" alt="image" src="https://github.com/user-attachments/assets/e186c36d-0577-49d1-a663-781851a31cc7" />
