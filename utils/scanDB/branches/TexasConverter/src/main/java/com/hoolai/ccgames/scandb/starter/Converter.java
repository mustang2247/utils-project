package com.hoolai.ccgames.scandb.starter;

import com.hoolai.ccgames.center.vo.ItemGiveList;
import com.hoolai.ccgames.center.vo.ItemGiveRecord;
import com.hoolai.ccgames.center.vo.ItemList;
import com.hoolai.ccgames.center.vo.ItemUnit;
import com.hoolai.ccgames.centergate.client.CenterRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoolai on 2016/9/23.
 */
public class Converter {

    private static final Logger logger = LoggerFactory.getLogger( Converter.class );

    public static Map< Integer, Long > sellItemIds = new HashMap<>();
    public static Map< Integer, Integer > convertItemIds = new HashMap<>();
    public static int NB_ITEM_ID = 10507;  // 牛币
    public static int XNB_ITEM_ID = 10508;  // 小牛币
    public static int CBT_ITEM_ID = 10301;  // 藏宝图
    public static CenterRepo centerRepo;

    static {
        // sell map
        sellItemIds.put( 465, 0L );
        sellItemIds.put( 1, 100L );
        sellItemIds.put( 2, 500L );
        sellItemIds.put( 3, 1000L );
        sellItemIds.put( 4, 20000L );
        sellItemIds.put( 5, 100000L );
        sellItemIds.put( 7, 200000L );
        sellItemIds.put( 8002, 230000L );
        sellItemIds.put( 8003, 230000L );
        sellItemIds.put( 104, 0L );
        sellItemIds.put( 220, 1000L );
        sellItemIds.put( 242, 100L );
        sellItemIds.put( 112, 0L );
        sellItemIds.put( 113, 2000L );
        sellItemIds.put( 211, 10000L );
        sellItemIds.put( 212, 50000L );
        sellItemIds.put( 213, 100000L );
        sellItemIds.put( 225, 0L );
        sellItemIds.put( 227, 5000L );
        sellItemIds.put( 228, 10000L );
        sellItemIds.put( 229, 50000L );
        sellItemIds.put( 230, 0L );
        sellItemIds.put( 231, 0L );
        sellItemIds.put( 232, 0L );
        sellItemIds.put( 233, 0L );
        sellItemIds.put( 234, 0L );
        sellItemIds.put( 235, 0L );
        sellItemIds.put( 236, 0L );
        sellItemIds.put( 237, 0L );
        sellItemIds.put( 238, 0L );
        sellItemIds.put( 201, 1350L );
        sellItemIds.put( 202, 4000L );
        sellItemIds.put( 203, 7000L );
        sellItemIds.put( 204, 10000L );
        sellItemIds.put( 313, 20000L );
        sellItemIds.put( 316, 100000L );
        sellItemIds.put( 206, 100L );
        sellItemIds.put( 207, 0L );
        sellItemIds.put( 208, 0L );
        sellItemIds.put( 209, 18500L );
        sellItemIds.put( 210, 55000L );
        sellItemIds.put( 240, 0L );
        sellItemIds.put( 241, 10000L );
        sellItemIds.put( 244, 10L );
        sellItemIds.put( 245, 0L );
        sellItemIds.put( 246, 0L );
        sellItemIds.put( 247, 0L );
        sellItemIds.put( 3010, 10000L );
        sellItemIds.put( 3020, 52000L );
        sellItemIds.put( 3030, 106000L );
        sellItemIds.put( 305, 12000L );
        sellItemIds.put( 306, 62000L );
        sellItemIds.put( 307, 126000L );
        sellItemIds.put( 308, 650000L );
        sellItemIds.put( 310, 60000L );
        sellItemIds.put( 250, 50000L );
        sellItemIds.put( 251, 220000L );
        sellItemIds.put( 252, 0L );
        sellItemIds.put( 258, 0L );
        sellItemIds.put( 257, 0L );
        sellItemIds.put( 256, 0L );
        sellItemIds.put( 255, 0L );
        sellItemIds.put( 254, 0L );
        sellItemIds.put( 253, 0L );
        sellItemIds.put( 2010, 900L );
        sellItemIds.put( 2020, 3600L );
        sellItemIds.put( 2030, 9000L );
        sellItemIds.put( 2040, 90000L );
        sellItemIds.put( 262, 0L );
        sellItemIds.put( 263, 300L );
        sellItemIds.put( 264, 30000L );
        sellItemIds.put( 265, 52000L );
        sellItemIds.put( 266, 106000L );
        sellItemIds.put( 267, 15000L );
        sellItemIds.put( 268, 0L );
        sellItemIds.put( 273, 2000L );
        sellItemIds.put( 269, 100000L );
        sellItemIds.put( 270, 106000L );
        sellItemIds.put( 271, 300000L );
        sellItemIds.put( 272, 600000L );
        sellItemIds.put( 274, 15000L );
        sellItemIds.put( 275, 150000L );
        sellItemIds.put( 276, 52000L );
        sellItemIds.put( 277, 220000L );
        sellItemIds.put( 278, 10000L );
        sellItemIds.put( 279, 0L );
        sellItemIds.put( 280, 70000L );
        sellItemIds.put( 281, 150000L );
        sellItemIds.put( 282, 400000L );
        sellItemIds.put( 391, 100L );
        sellItemIds.put( 392, 100L );
        sellItemIds.put( 393, 100L );
        sellItemIds.put( 394, 100L );
        sellItemIds.put( 395, 100L );
        sellItemIds.put( 396, 100L );
        sellItemIds.put( 397, 100L );
        sellItemIds.put( 398, 100L );
        sellItemIds.put( 399, 100L );
        sellItemIds.put( 400, 100L );
        sellItemIds.put( 401, 100L );
        sellItemIds.put( 402, 100L );
        sellItemIds.put( 403, 100L );
        sellItemIds.put( 404, 100L );
        sellItemIds.put( 405, 100L );
        sellItemIds.put( 406, 100L );
        sellItemIds.put( 390, 10000L );
        sellItemIds.put( 407, 25000L );
        sellItemIds.put( 408, 1500000L );
        sellItemIds.put( 409, 580000L );
        sellItemIds.put( 410, 220000L );
        sellItemIds.put( 411, 106000L );
        sellItemIds.put( 412, 5700L );
        sellItemIds.put( 413, 3800L );
        sellItemIds.put( 414, 1400L );
        sellItemIds.put( 419, 5000L );
        sellItemIds.put( 4191, 2000L );
        sellItemIds.put( 420, 1000L );
        sellItemIds.put( 422, 100L );
        sellItemIds.put( 423, 1000L );
        sellItemIds.put( 424, 20000L );
        sellItemIds.put( 425, 0L );
        sellItemIds.put( 426, 0L );
        sellItemIds.put( 427, 0L );
        sellItemIds.put( 428, 220000L );
        sellItemIds.put( 429, 580000L );
        sellItemIds.put( 430, 1500000L );
        sellItemIds.put( 431, 0L );
        sellItemIds.put( 432, 0L );
        sellItemIds.put( 433, 0L );
        sellItemIds.put( 434, 0L );
        sellItemIds.put( 435, 3000000L );
        sellItemIds.put( 436, 1500000L );
        sellItemIds.put( 437, 580000L );
        sellItemIds.put( 438, 220000L );
        sellItemIds.put( 439, 106000L );
        sellItemIds.put( 440, 52000L );
        sellItemIds.put( 441, 10000L );
        sellItemIds.put( 442, 50L );
        sellItemIds.put( 443, 50L );
        sellItemIds.put( 444, 50L );
        sellItemIds.put( 445, 50L );
        sellItemIds.put( 446, 50L );
        sellItemIds.put( 447, 50L );
        sellItemIds.put( 448, 50L );
        sellItemIds.put( 449, 50L );
        sellItemIds.put( 450, 50L );
        sellItemIds.put( 451, 50L );
        sellItemIds.put( 452, 50L );
        sellItemIds.put( 453, 50L );
        sellItemIds.put( 454, 50L );
        sellItemIds.put( 455, 50L );
        sellItemIds.put( 456, 50L );
        sellItemIds.put( 457, 50L );
        sellItemIds.put( 458, 1800L );
        sellItemIds.put( 466, 1770000L );
        sellItemIds.put( 467, 750000L );
        sellItemIds.put( 468, 300000L );
        sellItemIds.put( 469, 140000L );
        sellItemIds.put( 470, 80000L );
        sellItemIds.put( 471, 15000L );
        sellItemIds.put( 480, 3000000L );
        sellItemIds.put( 481, 1500000L );
        sellItemIds.put( 482, 580000L );
        sellItemIds.put( 483, 220000L );
        sellItemIds.put( 484, 106000L );
        sellItemIds.put( 503, 0L );
        sellItemIds.put( 504, 0L );
        sellItemIds.put( 505, 0L );
        sellItemIds.put( 506, 0L );
        sellItemIds.put( 507, 0L );
        sellItemIds.put( 508, 0L );
        sellItemIds.put( 509, 10000L );
        sellItemIds.put( 50900, 10000L );
        sellItemIds.put( 510, 0L );
        sellItemIds.put( 512, 150000L );
        sellItemIds.put( 513, 0L );
        sellItemIds.put( 514, 0L );
        sellItemIds.put( 515, 0L );
        sellItemIds.put( 516, 0L );
        sellItemIds.put( 517, 0L );
        sellItemIds.put( 518, 0L );
        sellItemIds.put( 519, 0L );
        sellItemIds.put( 520, 0L );
        sellItemIds.put( 521, 0L );
        sellItemIds.put( 522, 0L );
        sellItemIds.put( 523, 0L );
        sellItemIds.put( 524, 0L );
        sellItemIds.put( 525, 0L );
        sellItemIds.put( 526, 0L );
        sellItemIds.put( 527, 0L );
        sellItemIds.put( 528, 0L );
        sellItemIds.put( 529, 2000L );
        sellItemIds.put( 530, 1000L );
        sellItemIds.put( 531, 10000L );
        sellItemIds.put( 532, 100000L );
        sellItemIds.put( 533, 5000000L );
        sellItemIds.put( 534, 0L );
        sellItemIds.put( 535, 0L );
        sellItemIds.put( 536, 0L );
        sellItemIds.put( 537, 100L );
        sellItemIds.put( 538, 1000L );
        sellItemIds.put( 539, 10000L );
        sellItemIds.put( 540, 200000L );
        sellItemIds.put( 545, 0L );
        sellItemIds.put( 555, 20000L );
        sellItemIds.put( 556, 0L );
        sellItemIds.put( 557, 0L );
        sellItemIds.put( 558, 0L );
        sellItemIds.put( 559, 0L );
        sellItemIds.put( 560, 0L );
        sellItemIds.put( 561, 2000L );
        sellItemIds.put( 579, 0L );
        sellItemIds.put( 581, 2500L );
        sellItemIds.put( 582, 5L );
        sellItemIds.put( 583, 5L );
        sellItemIds.put( 584, 5L );
        sellItemIds.put( 585, 5L );
        sellItemIds.put( 586, 5L );
        sellItemIds.put( 587, 500L );
        sellItemIds.put( 607, 2000L );
        sellItemIds.put( 620, 2000L );
        sellItemIds.put( 621, 200000L );
        sellItemIds.put( 625, 2000L );
        sellItemIds.put( 626, 2000L );
        sellItemIds.put( 627, 50L );
        sellItemIds.put( 628, 50L );
        sellItemIds.put( 629, 50L );
        sellItemIds.put( 630, 50L );
        sellItemIds.put( 631, 50L );
        sellItemIds.put( 632, 50L );
        sellItemIds.put( 633, 50L );
        sellItemIds.put( 634, 2000L );
        sellItemIds.put( 5001, 0L );
        sellItemIds.put( 5002, 0L );
        sellItemIds.put( 5003, 0L );
        sellItemIds.put( 5004, 0L );
        sellItemIds.put( 5005, 0L );
        sellItemIds.put( 5006, 0L );
        sellItemIds.put( 5007, 0L );
        sellItemIds.put( 5008, 0L );
        sellItemIds.put( 5009, 0L );
        sellItemIds.put( 5010, 0L );
        sellItemIds.put( 5011, 0L );
        sellItemIds.put( 5012, 0L );
        sellItemIds.put( 5014, 0L );
        sellItemIds.put( 5015, 0L );
        sellItemIds.put( 5016, 0L );
        sellItemIds.put( 5019, 0L );
        sellItemIds.put( 5020, 0L );
        sellItemIds.put( 5021, 0L );
        sellItemIds.put( 5022, 0L );
        sellItemIds.put( 5023, 0L );
        sellItemIds.put( 5024, 0L );
        sellItemIds.put( 5025, 0L );
        sellItemIds.put( 5026, 0L );
        sellItemIds.put( 5027, 0L );
        sellItemIds.put( 5028, 0L );
        sellItemIds.put( 5029, 0L );
        sellItemIds.put( 5030, 0L );
        sellItemIds.put( 5031, 0L );
        sellItemIds.put( 5032, 0L );
        sellItemIds.put( 5033, 0L );
        sellItemIds.put( 5034, 0L );
        sellItemIds.put( 5035, 0L );
        sellItemIds.put( 5036, 0L );
        sellItemIds.put( 5037, 0L );
        sellItemIds.put( 5038, 0L );
        sellItemIds.put( 1610, 1000L );
        sellItemIds.put( 1618, 500000L );
        sellItemIds.put( 1619, 1000L );
        sellItemIds.put( 1620, 2500L );
        sellItemIds.put( 1621, 10000L );
        sellItemIds.put( 1622, 106000L );
        sellItemIds.put( 1623, 580000L );
        sellItemIds.put( 1628, 50L );
        sellItemIds.put( 1629, 80L );
        sellItemIds.put( 1630, 100L );
        sellItemIds.put( 1631, 0L );
        sellItemIds.put( 1632, 500L );
        sellItemIds.put( 6001, 400000L );
        sellItemIds.put( 6002, 120000L );
        sellItemIds.put( 6003, 55000L );
        sellItemIds.put( 6004, 15000L );
        sellItemIds.put( 6005, 2000L );
        sellItemIds.put( 6006, 2500L );
        sellItemIds.put( 7501, 1500000L );
        sellItemIds.put( 7502, 580000L );
        sellItemIds.put( 7503, 220000L );
        sellItemIds.put( 7504, 106000L );
        sellItemIds.put( 7505, 52000L );
        sellItemIds.put( 7506, 10000L );
        sellItemIds.put( 7507, 1650000L );
        sellItemIds.put( 7508, 650000L );
        sellItemIds.put( 7509, 250000L );
        sellItemIds.put( 7510, 130000L );
        sellItemIds.put( 7511, 60000L );
        sellItemIds.put( 7512, 12500L );
        sellItemIds.put( 7513, 3000000L );
        sellItemIds.put( 7514, 1500000L );
        sellItemIds.put( 7515, 650000L );
        sellItemIds.put( 7516, 250000L );
        sellItemIds.put( 7517, 110000L );
        sellItemIds.put( 7518, 22500L );

        // convert map
        convertItemIds.put( 261, 10200 );
        convertItemIds.put( 2209, 10100 );
        convertItemIds.put( 2210, 10102 );
        convertItemIds.put( 221, 10103 );
        convertItemIds.put( 222, 10105 );
        convertItemIds.put( 223, 10106 );
        convertItemIds.put( 9, 10502 );
        convertItemIds.put( 8, 10503 );
        convertItemIds.put( 8004, 10504 );
        convertItemIds.put( 9001, 10505 );
        convertItemIds.put( 8001, 10506 );
        convertItemIds.put( 500, 10301 );
        convertItemIds.put( 501, 10301 );
        convertItemIds.put( 636, 10301 );
        convertItemIds.put( 511, 10507 );
        convertItemIds.put( 5112, 10509 );
        convertItemIds.put( 5111, 10508 );
        convertItemIds.put( 5113, 10510 );
        convertItemIds.put( 598, 10511 );
        convertItemIds.put( 5981, 10512 );
        convertItemIds.put( 309, 10513 );
        convertItemIds.put( 301, 10514 );

    }

    public static void convertLevel( long uid, int oldLevel ) {
        long exp = centerRepo.getExp( uid );
        long hp = centerRepo.changeHp( uid, 50 );

        if( exp > 0 ) centerRepo.addExp( uid, -exp );
        logger.debug( "user {} oldlv {} newexp {} newhp {}", uid, oldLevel, exp, hp );

        if( oldLevel < 20 ) { // 1~19
            long rv = centerRepo.changeGold( uid, 500L );
            logger.debug( "user {} add gold 500 new gold {}", uid, rv );
        }
        else if( oldLevel < 30 ) { // 20~29
            long rv = centerRepo.addExp( uid, 130 );
            logger.debug( "user {} add exp 130 new exp {}", uid, rv );
            rv = centerRepo.changeGold( uid, 2000L );
            logger.debug( "user {} add gold 2000 new gold {}", uid, rv );
        }
        else if( oldLevel < 40 ) { // 30~39
            long rv = centerRepo.addExp( uid, 310 );
            logger.debug( "user {} add exp 310 new exp {}", uid, rv );
            ItemList list = new ItemList();
            list.changeNoCheck( XNB_ITEM_ID, 30, 0 );
            boolean res = centerRepo.changeItems( uid, list );
            logger.debug( "user {} add item {}*30 res {}", uid, XNB_ITEM_ID, res );
        }
        else if( oldLevel < 50 ) { // 40~49
            long rv = centerRepo.addExp( uid, 570 );
            logger.debug( "user {} add exp 570 new exp {}", uid, rv );
            ItemList list = new ItemList();
            list.changeNoCheck( NB_ITEM_ID, 8, 0 );
            boolean res = centerRepo.changeItems( uid, list );
            logger.debug( "user {} add item {}*8 res {}", uid, NB_ITEM_ID, res );
        }
        else {
            long rv = centerRepo.addExp( uid, 730 );
            logger.debug( "user {} add exp 730 new exp {}", uid, rv );
            ItemList list = new ItemList();
            list.changeNoCheck( NB_ITEM_ID, 15, 0 );
            boolean res = centerRepo.changeItems( uid, list );
            logger.debug( "user {} add item {}*15 res {}", uid, NB_ITEM_ID, res );
        }
    }

    public static void convertMP( long uid ) {
        long mp = centerRepo.getMasterPoint( uid );
        logger.debug( "user {} mp {}", uid, mp );
        if( mp > 0 ) {
            int itemCount = (int) mp * 4;
            ItemList list = new ItemList();
            list.changeNoCheck( CBT_ITEM_ID, itemCount, 0 );
            boolean res = centerRepo.changeItems( uid, list );
            logger.debug( "user {} add item {}*{} res {}", uid, CBT_ITEM_ID, itemCount, res );
        }
    }

    public static void convertPacks( long uid, ItemList packs ) {
        ItemList list = new ItemList();
        for( ItemUnit item : packs.items ) {
            Long val = sellItemIds.get( item.itemID );
            if( val != null ) {
                centerRepo.changeGold( uid, val * item.itemCount );
                logger.debug( "user {} sell item {}*{} add gold {}",
                        uid, item.itemID, item.itemCount, val * item.itemCount );
                continue;
            }
            Integer newId = convertItemIds.get( item.itemID );
            if( newId != null ) {
                int oldCount = item.itemCount;
                if( newId == CBT_ITEM_ID ) {
                    if( item.itemID == 500 ) item.itemCount *= 4;
                    if( item.itemID == 501 ) item.itemCount *= 40;
                    if( item.itemID == 636 ) item.itemCount *= 400;
                }

                logger.debug( "user {} convert item {}*{} to item {}*{}",
                        uid, item.itemID, oldCount, newId, item.itemCount );
                item.itemID = newId;
            }

            list.changeNoCheck( item.itemID, item.itemCount, item.expireTime );
        }
        centerRepo.changeItems( uid, list );
    }

    public static void convertUsed( long uid, ItemList used ) {
        ItemList list = new ItemList();
        for( ItemUnit item : used.items ) {
            Long val = sellItemIds.get( item.itemID );
            if( val != null ) {
                continue;
            }
            Integer newId = convertItemIds.get( item.itemID );
            if( newId != null ) {
                item.itemID = newId;
            }
            list.changeNoCheck( item.itemID, item.itemCount, item.expireTime );
        }
        centerRepo.addItemUsed( uid, list );
    }

    public static void convertGive( long uid, ItemGiveList gives ) {
        ItemGiveList list = new ItemGiveList();
        for( ItemGiveRecord item : gives.records ) {
            Long val = sellItemIds.get( item.itemID );
            if( val != null ) {
                continue;
            }
            Integer newId = convertItemIds.get( item.itemID );
            if( newId != null ) {
                item.itemID = newId;
            }
            // list.addRecord( item );
            centerRepo.addItemGive( uid, item );
        }

    }

}
