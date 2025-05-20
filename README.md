<div align="center" width="100%">
    <img src="./src/main/resources/static/images/android-chrome-512x512.png" width="200" height="200" alt="" />
</div>


# MeetingBuddies
## Application which can add virtual users to specified virtual confrencing applications
## Ready to use & easy-to-use self-hosted tool.

We will use the official Selenium Docker images:


- [`meeting-buddy`](https://hub.docker.com/repository/docker/adpndt/meeting-buddy): Check for updated docker imagge 
- [`selenium/hub`](https://hub.docker.com/r/selenium/hub): Central component to manage the grid.
- [`selenium/node-chrome`](https://hub.docker.com/r/selenium/node-chrome): Node image that connects Chrome to the Grid.

---

## 🚀 Quick Start

### 1. write docker file

```bash
    version: "3"
    services:
      selenium-hub:
        image: selenium/hub:4.20.0
        container_name: selenium-hub
        ports:
          - "4444:4444"
    
      chrome:
        image: selenium/node-chrome:4.20.0
        shm_size: 2gb
        depends_on:
          - selenium-hub
        environment:
          - SE_EVENT_BUS_HOST=selenium-hub
          - SE_EVENT_BUS_PUBLISH_PORT=4442
          - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
          - SE_NODE_MAX_SESSIONS=100
          - SE_NODE_OVERRIDE_MAX_SESSIONS=true
          - SE_NODE_MAX_SESSIONS=100
    
      meeting-buddy:
        image: adpndt/meeting-buddy:main
        container_name: meeting-buddy
        volumes:
          - ./data:/app/data
        ports:
          - "8666:8080"
        environment:
          - SPRING_PROFILE=prod
          - APP_PORT=8080
          - DB_DLL=update
          - SHOW_SQL=false
          - SELENIUM_GRID_URL=http://selenium-hub:4444/wd/hub
bash
### 2. Doploy docker file

```bash
docker-compose up -d
bash

### 3. Go to login page

```bash
http://localhost:8082/login
bash

### 4. Login creds
```bash
username -> admin
password -> admin123
bash






