package netty.test;

import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        ByteBuf in = (ByteBuf) msg;
        try {
            *//*
             * 这个低效的循环实际上可以简化为：System.out.println（in.toString（io.netty.util.CharsetUtil.US_ASCII））
             *//*
            while (in.isReadable()) { // (1)
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            *//*
             * 或者，你可以在这里做in.release（）。
             *//*
            ReferenceCountUtil.release(msg); // (2)
        }
    }*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*
         * ChannelHandlerContext对象提供各种操作，使您能够触发各种I / O事件和操作。
         * 在这里，我们调用write（Object）来逐字写入接收到的消息。
         * 请注意，我们没有发布收到的消息，这与我们在DISCARD示例中的操作不同。
         * 这是因为Netty在写入线路时会为您发布。
         */
        ctx.write(msg); // (1)
        /*
         * ctx.write（Object）不会将消息写入线路。
         * 它在内部缓冲，然后通过ctx.flush（）刷新到线路。
         * 或者，您可以调用ctx.writeAndFlush（msg）以简洁起见。
         */
        ctx.flush(); // (2)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}