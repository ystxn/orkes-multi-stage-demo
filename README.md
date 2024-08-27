# Multi-Stage Example

This example demonstrates how to implement a multi-stage parent-child workflow using a simple backend and frontend that communicates via Server-Sent Events.

## Workflows
- [MultiStageFlow](workflows/MultiStageFlow.json)
- [SubFlowA](workflows/SubFlowA.json)
- [SubFlowB](workflows/SubFlowB.json)
- [SubFlowC](workflows/SubFlowC.json)

## Backend Service
- [BackendService](src/main/java/io/orkes/examples/BackendService.java)

## Frontend
- [index.htm](src/main/resources/static/index.htm)

## Reverse Proxy
[ngrok](https://ngrok.com) used to proxy a public endpoint to localhost so that Conductor can call the webhook
