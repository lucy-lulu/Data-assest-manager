# DA-Quoll - course number swen90014
![Static Badge](https://img.shields.io/badge/language-Java-blue)
![Static Badge](https://img.shields.io/badge/framework-spring%20boot-green)

![Static Badge](https://img.shields.io/badge/language-JavaScript-blue)
![Static Badge](https://img.shields.io/badge/framework-vue-green)


# Project Background
Over the years, The Melbourne School of Professional and Continuing Education's (MSPACE) repository of Learning and Teaching (L&T) materials has expanded significantly, presenting a challenge in efficiently tracking and identifying existing assets. Consequently, they often find themselves rebuilding from scratch rather than leveraging and enhancing existing resources. To tackle this issue, there's a growing interest within their team to develop a Digital Asset Manager (DAM). However, the manual effort required to populate and maintain such a system poses a significant resource constraint. Furthermore, incomplete or outdated data could diminish the value of the DAM.

To address this challenge, we have developed a system that helps users manually create, search for, and collect the teaching resources they need, simplifying the process of resource management and access. Users can upload their own created resources for others to use, and the system supports generating resource details automatically through Canvas file URLs and creating assets in the database, speeding up the resource upload process. In future development, we will also consider integrating AI tools to further enhance the system's capabilities.


# Handover
As we conclude the development of the project, we are ensuring a smooth transition for future maintenance and further enhancements. We have deployed our product to cloud, and can be visit here: http://34.129.9.77/login This cloud deployment is available until 08/12/2024, apart from the delpoy, we also conclude useful documentation for further development, the final handover includes:

 **1. Local Deploper documentation**
This document provides step-by-step instructions for deploying the system locally on a development machine. It details the system requirements, setup process, and configuration steps necessary to ensure a smooth and reliable local environment.

Link: [Developer documentation](https://github.com/feit-swen90014/DA-Quoll/blob/main/docs/handover/Developer%20Documentation.md)
 
 **2. API documentation**
A detailed guide for developers and integrators, this document outlines the available API endpoints, request/response formats, authentication methods, and example API calls. It will help developers integrate other systems or extend the functionality of the application.

Link: [API documentation](https://github.com/feit-swen90014/DA-Quoll/blob/main/docs/handover/API%20document_final.pdf)
 
 **3. User Manual**
This is a user-centric document that guides end-users through the various features and functionalities of the system. It provides clear, practical instructions on how to navigate the system, perform tasks such as asset management, and utilize the available tools effectively.

Link: [User manual](https://github.com/feit-swen90014/DA-Quoll/blob/main/docs/handover/User%20manual.md)
 
  **4. Cloud Deployment documentation**
Cloud Deployment Documentation: For those looking to deploy the system in a cloud environment, this document contains instructions on setting up the system on cloud platforms, including necessary configurations, security considerations, and scalability options.

Link: [Manual deployment to cloud](https://github.com/feit-swen90014/DA-Quoll/blob/main/docs/handover/Manual%20Deployment%20Documentation.md)
 

Together, these documents ensure that all stakeholders have the necessary resources to deploy, operate, and manage the system efficiently.

As for the showcase of final product, it is stored in the deliverables folder here: [deliverables](https://github.com/feit-swen90014/DA-Quoll/tree/main/docs/deliverables)

## Project Board
The project board in the DA-Quoll project serves as a dynamic tool for managing and tracking the progress of tasks, recording sprint backlogs for each sprint(planned backlogs as well), and also ensuring the project remains on schedule and that all team members are aligned with the project's goals. By visualizing the workflow, from task assignment to completion, it allows the team to identify bottlenecks, prioritize tasks, and allocate resources effectively. It acts as a real-time dashboard that provides a clear overview of what tasks are in progress, which ones are pending, and what has been completed, fostering accountability and transparency within the team. By providing a centralized platform for project management, the project board not only streamlines the execution process but also ensures that the project stays on track towards its objectives.

Visit our project board here: [DA-Quoll Project Board](https://github.com/orgs/feit-swen90014/projects/10)


## Wiki Page
The wiki page for the this project is a critical resource that consolidates all project documentation, facilitating seamless collaboration, knowledge sharing, and process alignment among team members. By serving as a central hub for information, it ensures that everyone involved in the project has access to the latest details, decisions, and technical guidelines. This helps to maintain consistency across the project's development stages and supports effective onboarding for new team members. Our team tend to use "10-seconds test" to verify the use and comperhensive of our wiki page design.

Visit our wiki page here: [DA-Quoll Wiki Page](https://github.com/feit-swen90014/DA-Quoll/wiki)

## Changelog

### Sprint #1
- Finalized the design concept and presented it using a domain model.
- Made decisions on technology stack and documented the choices.
- Developed a comprehensive reuse plan.
- Created the test plan and acceptance narrative document.
- Designed the project architecture and presented it using the 4+1 view model.
- Created project code frame:
  
    -frontend: Implemented using the Vue framework.
  
    -backend: Built with the Spring Boot framework.
  
    -Created the SQL scripts for database initialization.
- Compiled meeting minutes for Sprint 1 planning, retrospective, review, and team stand-up meetings.

### Sprint #2
- Backend functions development:
  
    -Create, remove, edit, get assets (US4, 5, 6).

    -Collect and categorizing assets (US19, 20).

    -Search, filter, sort assets (US8, 9, 10).

    -Add README.md file for back-end.
- Frontend pages development and integration with backend:
  
    -Designed teacher dashboard page and integrate it with search, filter, sort function (US2, 8, 9, 10).

    -Designed asset detail page and integrate it with create, get assets (US4, 6).

    -Design static pages for collect assets and login.

    -Add README.md file for front-end.
- Update ER diagram and detail based on the latest database structure.
- Conducted unit tests and documented unit test plan.
- Conducted integration tests and documented integration test plan.
- Create Issue tracking and workflow document.
- Compiled meeting minutes for Sprint 2 planning, retrospective, review, and team stand-up meetings.

### Sprint #3
-   Backend functions development:
    
    -Realize login function using JWT token.
    
    -Generate report for assets.
    
    -Add feedback function.
    
    -Realize fetch asset from canvas using canvas API.
    
-   Frontend pages development and integration with backend:
    
    -Integrate login function for different user roles, and designed and realize different dashboard for corresponding role.
    
    -Collect and categorizing assets for teacher dashboard(US19, 20).
    
    -Design and integrate feedback function and generate report function.
    
    -Integrate edit assets detail function.
    
-   Update architecture documentation based on the revision we make, document the change logs in document.
    
-   Conducted unit tests and documented unit test plan.
    
-   Conducted integration tests and documented integration test plan.
    
-   Create developer documentation, Manual deployment documentation, and user manual for handover.
-  Upload the final version of API document, and delete the version for sprint2.
- Upload the video showing final demo and document corresponding deliverables
    
-   Compiled meeting minutes for Sprint 3 planning, retrospective, review, and team stand-up meetings.

## Project Structure
```
├── README.md
├── docs
│   ├── Product backlogs.md
│   ├── Reuse plan.md
│   ├── architecture
│   │   ├── Architecture_candidate.md
│   │   ├── Architecture_diagram.md
│   │   ├── Class_diagram.md
│   │   ├── Component_design.md
│   │   ├── Design_concept-Domain_model.md
│   │   ├── ER_diagram.md
│   │   ├── Flow_chart.md
│   │   ├── README.md
│   │   ├── Sequence_diagram.md
│   │   ├── Use_case_diagram.md
│   │   └── resources
│   │       ├── ...
│   ├── meetings
│   │   ├── #meeting minutes template.md
│   │   ├── Sprint 1
│   │   │   ├── Sprint Planning.md
│   │   │   ├── Sprint Retrospective.md
│   │   │   └── Sprint Review.md
│   │   ├── mentor meetings
│   │   │   ├── ...
│   │   └── team meetings
│   │       ├── ...
│   ├── technical stack choice
│   │   ├── Back-end_tech_choice.md
│   │   ├── Database_tech_choice.md
│   │   ├── Front-end_tech_choice.md
│   │   └── README.md
│   └── tests
│       ├── Acceptance Narrative.md
│       └── Test plan.md
└── src
    ├── back-end                                                      # back-end code framework
    │   └── DAMS
    │       ├── ...
    │       └── src
    │           ├── main
    │           │   ├── java
    │           │   │   └── SWEN_90014
    │           │   │       └── DAMS
    │           │   │           ├── DamsApplication.java
    │           │   │           └── Model
    │           │   │               ├── Administrator.java
    │           │   │               ├── Assert.java
    │           │   │               ├── BrowseAssert.java
    │           │   │               ├── Feedback.java
    │           │   │               ├── Login.java
    │           │   │               ├── Report.java
    │           │   │               ├── Tagging.java
    │           │   │               ├── Teacher.java
    │           │   │               └── User.java
    │           │   └── resources
    │           │       └── application.properties
    │           └── test
    │               └── java
    │                   └── SWEN_90014
    │                       └── DAMS
    │                           └── DamsApplicationTests.java
    ├── database                                                       # database initialization
    │   ├── init.sql
    │   └── loadData.sql
    └── front-end                                                      # front-end code framework
        ├── README.md
        ├── ...
        ├── src
        │   ├── App.vue
        │   ├── assets
        │   │   └── logo.png
        │   ├── components
        │   │   └── HelloWorld.vue
        │   ├── main.js
        │   ├── router
        │   │   └── index.js
        │   └── views
        │       ├── AboutView.vue
        │       └── HomeView.vue
        └── vue.config.js
```


## Acknowledgments
This Digital Asset Manager (DAM) product is the exclusive intellectual property of MSPACE. We hereby extend our gratitude to MSPACE for providing the foundational infrastructure upon which our team is developing an automation add-on. Without their explicit support and allocation of resources, the realisation of this project would not have been feasible.

Moreover, we wish to formally acknowledge the invaluable contributions made by all team members involved in the development of the digital asset manager during phase two. Their sustained dedication, expert proficiency, and collaborative ethos have been indispensable in moulding this innovative solution.
