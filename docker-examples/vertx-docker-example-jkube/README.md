# Vert.x Docker Example for Fabric8

This project builds a docker image launching a very simple Vert.x verticle that you can deploy to
[Kubernetes](http://kubernetes.io/) using the [Eclipse JKube](https://www.eclipse.org/jkube)
[Kubernetes-maven-plugin](https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin).

## Build the image

To [build](https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin#jkube:build) the docker image, just launch:

```shell script
$ mvn clean package k8s:build
```

Notice that you need to have docker installed on your machine. If you are planning to deploy the application into a
cluster you'll either have to [push](#Pushing-the-image) your image to a shared registry or build the image using the
cluster's Docker daemon.
```shell script
# Use Minikube Docker daemon to build the image
$ eval $(minikube docker-env)
```
## Deployment on Kubernetes

In order to deploy the application into Kubernetes you'll need to build
[resource](https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin#jkube:resource) manifests and
[apply](https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin#jkube:apply) them to the cluster.

The following Kubernetes-Maven-Plugin goals will do that for you:

```shell script
$ mvn k8s:resource k8s:apply
```

## Pushing the image

If you want to deploy the image into a remote cluster (not Minikube), you'll need to
[push](https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin#jkube:push) the image to a Docker registry accessible
from the cluster.

You'll probably need to change the image name so that you can push to your specific organization, you can accomplish that
by using the `jkube.generator.name` JKube Maven property. You will also need to perform a `docker login` if your registry
requires authentication.

```shell script
$ mvn -Djkube.generator.name=${organization}/${name} k8s:build k8s:push k8s:resource k8s:apply
```

## Trying it out

You can check out the newly created pod, deployment and service by running:
```shell script
$ kubectl get svc,pod,deploy
```

If you are using Minikube, you can perform a request to the deployed Pod container by running:
```shell script
$  curl $(minikube ip):$(kubectl get svc vertx-docker-example-jkube -n default -o jsonpath='{.spec.ports[].nodePort}')
```

You can retrieve the application [log](https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin#jkube:log) by running:
```shell script
$ mvn k8s:log
```

When you are finished you can clean the generated resources and remove the deployment from your cluster by running:
```shell script
$ mvn k8s:undeploy
```