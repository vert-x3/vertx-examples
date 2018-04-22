import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;

/*
    Represents Vert.x instance running on a node (node1)
    This class deploys sender verticle on node1 (machine1)
 */
public class MainClassNode1 {
    public static void main(String[] args) {
        JsonObject zkConfig = configureClusterManager();
        ClusterManager zookeeperClusterManager = new ZookeeperClusterManager(zkConfig);

        VertxOptions options = configureVertx(zookeeperClusterManager);
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(2);
                vertx.deployVerticle("verticle.SenderVerticle", deploymentOptions);
                System.out.println("Sender verticle deployed");
            }
        });
    }

    /**
     * create configuration object to be used to configure the Zookeeper cluster manager
     * @return JsonObject representing Zookeeper configuration
     */
    private static JsonObject configureClusterManager() {
        /*
            Set zookeeperHosts property to the IPs of machines running the cluster manager, here we set it to localhost
            (127.0.0.1), but in case we have multiple machines/docker containers we have to set it on every node to the IPs
            of the machines running the cluster manager. For example:
            zkConfig.put("zookeeperHosts", "192.168.1.12"); // Zookeeper is running on this machines
            zkConfig.put("zookeeperHosts", "192.168.1.12,192.168.1.56");
         */
        JsonObject zkConfig = new JsonObject();
        zkConfig.put("zookeeperHosts", "localhost");
        zkConfig.put("rootPath", "io.vertx");
        zkConfig.put("retry", new JsonObject()
                .put("initialSleepTime", 3000)
                .put("maxTimes", 3));
        return zkConfig;
    }

    /**
     * Specifies Vert.x instance configuration, this is essential for clustering on multiple separate machines and
     * Docker containers in order to make Event Bus send/consume messages appropriately
     * @param clusterManager represents the cluster manager
     * @return VertxOptions object to be used in deployment
     */
    private static VertxOptions configureVertx(ClusterManager clusterManager) {
        /*
            the default value of the cluster host (localhost) is used here, if we want to have multiple machines/docker
            containers in our cluster we must configure the cluster host properly on each node in order for the event bus
            to send/consume messages properly between our verticles, to do so we use the method setClusterHost and give it
            this node's IP. For example:
            options.setClusterHost(192.168.1.12);
        */
        VertxOptions options = new VertxOptions()
                .setClustered(true)
                .setClusterManager(clusterManager);
        return options;
    }
}
