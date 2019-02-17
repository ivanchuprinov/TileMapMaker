package hapless.eagles.server;

import hapless.eagles.common.World;
import hapless.eagles.common.packets.serverbound.IServerPacketHandler;
import hapless.eagles.common.utils.Config;
import hapless.eagles.server.network.ServerInitHandler;
import hapless.eagles.server.network.ServerPacketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.DefaultEventExecutor;
import lombok.Getter;

/**
 * Represents a game server instance.
 * Created by Kneesnap on 2/16/2019.
 */
@Getter
public class ServerInstance {
    private ChannelGroup clients;
    private IServerPacketHandler packetHandler;
    private World world;
    private String name;
    private int port;

    public ServerInstance(Config config) {
        this.load(config);
    }

    /**
     * Load this instance from a config.
     * @param config config
     */
    public void load(Config config) {
        this.name = config.getString("name");
        this.port = config.getInt("port");

        // Load the world from the same config.
        this.world = new World();
        this.world.load(config);
    }

    /**
     * Start listening for connections.
     */
    public void startServer() {
        EventLoopGroup group = new NioEventLoopGroup();
        clients = new DefaultChannelGroup(new DefaultEventExecutor());

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(group)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ObjectEncoder())
                .childHandler(new ObjectDecoder(ClassResolvers.weakCachingResolver(getClass().getClassLoader())))
                .childHandler(new ServerInitHandler(this));

        this.packetHandler = new ServerPacketHandler(this);
        bootstrap.bind(this.port).channel().closeFuture().addListener(evt -> {
            group.shutdownGracefully();
            System.out.println(getName() + " is shutting down.");
        });

        System.out.println(getName() + " started on port " + getPort() + ".");
    }

}
