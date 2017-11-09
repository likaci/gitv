package com.gala.video.lib.share.uikit.data.flatbuffers;

import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.Card;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.CardMap;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.CardStyle;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.Item;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.Row;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout.FlatCard;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout.FlatCardList;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout.FlatCardStyle;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout.FlatItem;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout.FlatRow;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CardListBuilder {
    public CardMap buildCardList(ByteBuffer bf) {
        CardMap cardMap = new CardMap();
        FlatCardList flatCardList = FlatCardList.getRootAsFlatCardList(bf);
        for (int i = 0; i < flatCardList.cardlistLength(); i++) {
            Card card = buildCard(flatCardList.cardlist(i));
            if (card != null) {
                cardMap.put(card.layout_id, card.layout_style);
            }
        }
        return cardMap;
    }

    private Card buildCard(FlatCard flatCard) {
        Card card = new Card();
        card.layout_style = buildCardStyle(flatCard.layoutStyle());
        card.layout_id = flatCard.layoutId();
        return card;
    }

    private CardStyle buildCardStyle(FlatCardStyle flatCardStyle) {
        if (flatCardStyle == null) {
            return null;
        }
        CardStyle cardStyle = new CardStyle();
        cardStyle.f2039w = ResourceUtil.getPxShort(flatCardStyle.m1580w());
        cardStyle.body_h = ResourceUtil.getPxShort(flatCardStyle.bodyH());
        cardStyle.space_h = ResourceUtil.getPxShort(flatCardStyle.spaceH());
        cardStyle.space_v = ResourceUtil.getPxShort(flatCardStyle.spaceV());
        cardStyle.body_mg_b = ResourceUtil.getPxShort(flatCardStyle.bodyMgB());
        cardStyle.body_mg_l = ResourceUtil.getPxShort(flatCardStyle.bodyMgL());
        cardStyle.body_mg_r = ResourceUtil.getPxShort(flatCardStyle.bodyMgR());
        cardStyle.body_mg_t = ResourceUtil.getPxShort(flatCardStyle.bodyMgT());
        cardStyle.body_pd_b = ResourceUtil.getPxShort(flatCardStyle.bodyPdB());
        cardStyle.body_pd_l = ResourceUtil.getPxShort(flatCardStyle.bodyPdL());
        cardStyle.body_pd_r = ResourceUtil.getPxShort(flatCardStyle.bodyPdR());
        cardStyle.body_pd_t = ResourceUtil.getPxShort(flatCardStyle.bodyPdT());
        cardStyle.header_h = ResourceUtil.getPxShort(flatCardStyle.headerH());
        cardStyle.header_pd_b = ResourceUtil.getPxShort(flatCardStyle.headerPdB());
        cardStyle.header_pd_l = ResourceUtil.getPxShort(flatCardStyle.headerPdL());
        cardStyle.header_pd_r = ResourceUtil.getPxShort(flatCardStyle.headerPdR());
        cardStyle.header_pd_t = ResourceUtil.getPxShort(flatCardStyle.headerPdT());
        cardStyle.default_focus = flatCardStyle.defaultFocus();
        cardStyle.row_nolimit = flatCardStyle.rowNolimit();
        cardStyle.scale = flatCardStyle.scale();
        cardStyle.show_position = flatCardStyle.showPosition();
        cardStyle.type = flatCardStyle.type();
        cardStyle.backId = flatCardStyle.backId();
        List<Row> rowList = new ArrayList();
        for (int i = 0; i < flatCardStyle.rowsLength(); i++) {
            rowList.add(buildRow(flatCardStyle.rows(i)));
        }
        cardStyle.rows = rowList;
        return cardStyle;
    }

    private Row buildRow(FlatRow flatRow) {
        if (flatRow == null) {
            return null;
        }
        Row row = new Row();
        List<Item> itemList = new ArrayList();
        for (int i = 0; i < flatRow.itemsLength(); i++) {
            itemList.add(buildItem(flatRow.items(i)));
        }
        row.items = itemList;
        return row;
    }

    private Item buildItem(FlatItem flatItem) {
        if (flatItem == null) {
            return null;
        }
        Item item = new Item();
        item.scale = flatItem.scale();
        item.style = flatItem.style();
        item.f2041w = ResourceUtil.getPxShort(flatItem.m1582w());
        item.f2040h = ResourceUtil.getPxShort(flatItem.m1581h());
        item.space_h = ResourceUtil.getPxShort(flatItem.spaceH());
        item.space_v = ResourceUtil.getPxShort(flatItem.spaceV());
        return item;
    }
}
