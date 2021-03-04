# CombatSimulator
The purpose of this software is to simulate a "Pokemon 2v2 combat"-esque data offload.

Docker will spin up a series of containers which will interact together to
render a real time dashboard in Grafana of the simulation. 

Multiple simulations can be triggered by entering the combat-sim container and re-running the .jar file under the app directory.

# Getting Started

1. run 'docker-compose build --no-cache'
2. run 'docker-compose up -d'

This should get all containers started. Once started, ensure that StreamSets
and Grafana are appropriately provisioned. StreamSets may require you to 
link an account to start pipelines, which can be done free of charge as this
software does not use any of their ControlHub functionality.

Check on StreamSets status by visiting: http://localhost:18630.
For first time set up on the "Create New Pipeline" button dropdown
select "Import Pipelines From Archive" and then choose the 
"StreamSetPipelines.zip" file included with this project. 

Once imported, the pipelines can be started by selecting all pipelines
and clicking the play button in the top right of the screen.

Finally visit the grafana page by visiting http://localhost:3000 using the 
credentials listed in the .env file.

There should be a CombatSimulation Dashboard already provisioned, but in
case there isn't it can be imported by hovering the 4 square tile icon on 
the left menu for dashboards, going to manage, then import, and then 
loading the .json file found under 
grafana/provisioning/dashboards/general/CombatSimulationDashboard.json

Once on the Combat Dashboard, it can be set to auto refresh every 5s
by using the dropdown in the top right corner.


# Notes
Should you wish to update the .env file to reference other passwords, StreamSets configuration, and some java configuration (such as the postgres connect files)
will also need to be updated appropriately. Any major change to the dockerbuild
or the java code should be followed with running 'docker-compose build --no-cache' again.
