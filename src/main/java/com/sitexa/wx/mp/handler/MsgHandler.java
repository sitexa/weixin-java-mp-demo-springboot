package com.sitexa.wx.mp.handler;

import com.sitexa.wx.mp.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                && weixinService.getKefuService().kfOnlineList()
                .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
            }

            if (StringUtils.startsWithAny(wxMessage.getContent(), "1")) {
                StringBuffer content = new StringBuffer();
                content.append("文章列表:\n");
                content.append("1,<a href='http://www.sitexa.net/post/blockchain-analyst/'>区块链：理想还是梦幻</a>\n");
                content.append("2,<a href='https://www.zhihu.com/question/268216396/answer/661250212'>30岁以上的程序员该何去何从？</a>\n");
                return new TextBuilder().build(content.toString(), wxMessage, weixinService);
            } else if (StringUtils.startsWithAny(wxMessage.getContent(), "2")) {
                return new TextBuilder().build("课程列表", wxMessage, weixinService);
            } else if (StringUtils.startsWithAny(wxMessage.getContent(), "3","4","5","6","7","8","9")) {
                return new TextBuilder().build("彩蛋来也", wxMessage, weixinService);
            }else{
                return new TextBuilder().build("感谢关注，敬请期待！",wxMessage,weixinService);
            }


        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        //TODO 组装回复消息
        //String content = "收到信息内容：" + JsonUtils.toJson(wxMessage);
        String content = "收到信息内容：" + wxMessage.getContent();

        return new TextBuilder().build(content, wxMessage, weixinService);

    }

}
