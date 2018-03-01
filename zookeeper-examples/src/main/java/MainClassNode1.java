import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;

import java.net.UnknownHostException;

/*
    Represents Vert.x instance running on a node (node1)
    This class deploys sender verticle on node1 (machine1)
 */
public class MainClassNode1 {
    public static void main(String[] args) throws UnknownHostException {
        JsonObject zkConfig = configureClusterManager();
        ClusterManager zookeeperClusterManager = new ZookeeperClusterManager(zkConfig);

        VertxOptions options = configureVertx(zookeeperClusterManager);
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(2);
                vertx.deployVerticle("verticle.SenderVerticle", deploymentOptions);
            }
        });
    }

    /**
     * create configuration object to be used to configure the Zookeeper cluster manager
     * @return JsonObject representing Zookeeper configuration
     */
    private static JsonObject configureClusterManager() {
        // add the IP of the machine hosting the cluster manager
        JsonObject zkConfig = new JsonObject();
        zkConfig.put("zookeeperHosts", "192.168.1.12");
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
        // add this machine's IP as clusterHost
        // if vertx instances run on the same machine then we must use different port for each instance
        // else if working on different separate machines it doesn't matter
        VertxOptions options = new VertxOptions()
                .setClustered(true)
                .setClusterHost("192.168.1.12")
                .setClusterPort(17001)
                .setClusterManager(clusterManager);
        return options;
    }
}
