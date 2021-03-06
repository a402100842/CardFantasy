package cfvbaibai.cardfantasy.test.func;

import org.junit.Assert;
import org.junit.Test;

import cfvbaibai.cardfantasy.data.Race;
import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.CardStatusType;

public class SpecialStatusTest extends SkillValidationTest {
    /**
     * 被冰冻时无法回春
     */
    @Test
    public void test冰冻_回春() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "水源制造者-5", "凤凰");
        context.addToField(0, 0);
        CardInfo c凤凰 = context.addToField(1, 1);
        context.startGame();

        random.addNextNumbers(0).addNextPicks(0); // 水源冰冻凤凰
        context.proceedOneRound();

        context.proceedOneRound();
        Assert.assertEquals(310 + 80, 1560 - c凤凰.getHP());
    }

    /**
     * 被锁定时无法回春
     */
    @Test
    public void test锁定_回春() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "地岭拥有者-5", "凤凰");
        context.addToField(0, 0);
        CardInfo c凤凰 = context.addToField(1, 1);
        context.startGame();

        random.addNextNumbers(0).addNextPicks(0); // 地岭拥有者锁定凤凰
        context.proceedOneRound();

        context.proceedOneRound();
        Assert.assertEquals(335, 1560 - c凤凰.getHP());
    }
    
    /**
     * 被锁定时仍能发动盾刺
     */
    @Test
    public void test锁定_盾刺() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "秘银巨石像+陷阱1", "占位符+盾刺10");
        CardInfo c秘银巨石像 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        random.addNextNumbers(0).addNextPicks(0); // 锁定
        context.proceedOneRound();
        Assert.assertEquals(200 /* 盾刺 */, 1550 - c秘银巨石像.getHP());
    }

    /**
     * 被锁定时仍能发动邪灵汲取
     */
    @Test
    public void test锁定_邪灵汲取() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "秘银巨石像+陷阱1", "占位符+邪灵汲取10");
        CardInfo c秘银巨石像 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        random.addNextNumbers(0).addNextPicks(0); // 锁定
        context.proceedOneRound();
        Assert.assertEquals(243 /* 邪灵汲取 */, 810 - c秘银巨石像.getCurrentAT());
    }

    /**
     * 被麻痹时仍能回春
     */
    @Test
    public void test麻痹_回春() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "风暴召唤者-5", "凤凰");
        context.addToField(0, 0);
        CardInfo c凤凰 = context.addToField(1, 1);
        context.startGame();

        random.addNextNumbers(0).addNextPicks(0); // 风暴召唤者麻痹凤凰
        context.proceedOneRound();

        random.addNextNumbers(0).addNextPicks(0); // 凤凰烈焰风暴
        context.proceedOneRound();
        Assert.assertEquals(300 + 100 - 210, 1560 - c凤凰.getHP());
    }
    
    /**
     * 冰冻+迷魂状态下，不可回春，但仍会攻击自己英雄
     */
    @Test
    public void test冰冻_迷魂_回春() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "水源制造者-1", "彩翼公主", "凤凰");
        context.addToField(0, 0);
        context.addToField(1, 0);
        CardInfo c凤凰 = context.addToField(2, 1);
        context.startGame();

        random.addNextPicks(0).addNextNumbers(0);   // 水源制造者冰冻
        random.addNextPicks(0).addNextNumbers(0);   // 彩翼公主迷魂
        context.proceedOneRound();

        context.proceedOneRound();

        Assert.assertEquals(
            435 /* 彩翼公主的攻击 */ + 660 /* 凤凰被迷魂的攻击 */,
            6390 - context.getPlayer(1).getHP());
        Assert.assertEquals(80 /* 水源冰弹 */ + 218 /* 水源攻击 */ - 0 /* 无法回春 */, 1560 - c凤凰.getHP());
    }
    
    /**
     * 燃烧比回春先结算
     */
    @Test
    public void test燃烧_回春() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "地狱红龙", "凤凰-5");
        context.addToField(0, 0);
        context.addToField(1, 1).setBasicHP(691);
        context.startGame();
        
        random.addNextPicks(0);     // 地狱红龙烈火焚神
        context.proceedOneRound();

        random.addNextPicks(0).addNextNumbers(0); // 凤凰烈焰风暴
        context.proceedOneRound();

        // 燃烧和回春结算前，凤凰HP还剩下: 691 - 540 - 150 = 1
        // 由于燃烧先结算，所以凤凰无法回春而死亡。
        Assert.assertEquals(0, context.getPlayer(1).getField().size());
    }
    
    /**
     * 中毒比回春先结算
     */
    @Test
    public void test中毒_回春() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "蝎尾狮", "凤凰-5");
        context.addToField(0, 0);
        context.addToField(1, 1).setBasicHP(801);
        context.startGame();
        
        random.addNextPicks(0);     // 蝎尾狮毒液
        context.proceedOneRound();

        random.addNextPicks(0).addNextNumbers(0); // 凤凰烈焰风暴
        context.proceedOneRound();

        // 中毒和回春结算前，凤凰HP还剩下: 801 - 480 (攻击力) - 200 (背刺) - 120 (毒液) = 1
        // 由于中毒先结算，所以凤凰无法回春而死亡。
        Assert.assertEquals(0, context.getPlayer(1).getField().size());
    }
    
    /**
     * 冰冻麻痹同时中的话，也会同时解除
     */
    @Test
    public void test冰冻_麻痹() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "水源制造者-1", "狮鹫-5", "凤凰");
        context.addToField(0, 0);
        context.addToField(1, 0);
        CardInfo c凤凰 = context.addToField(2, 1);
        context.startGame();
        
        random.addNextPicks(0).addNextNumbers(0);   // 水源制造者冰弹打凤凰，保证冰冻
        random.addNextPicks(0).addNextNumbers(0);   // 狮鹫连环闪电打凤凰，保证麻痹
        context.proceedOneRound();
        
        context.proceedOneRound();
        Assert.assertFalse("凤凰的麻痹应该已经解除", c凤凰.getStatus().containsStatus(CardStatusType.麻痹));
        Assert.assertFalse("凤凰的冰冻应该已经解除", c凤凰.getStatus().containsStatus(CardStatusType.冰冻));
    }

    /**
     * 基本的虚弱测试
     */
    @Test
    public void test虚弱() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "东方幻术师-1", "秘银巨石像");
        CardInfo c东方幻术师 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        random.addNextPicks(0); // 东方幻术师对秘银巨石像使用虚弱
        context.proceedOneRound();
        
        context.proceedOneRound();
        Assert.assertEquals(330, 1056 - c东方幻术师.getHP());
    }
    
    /**
     * 晕眩状态下被迷魂，仍然会攻击自己英雄
     */
    @Test
    public void test虚弱_晕眩_迷魂() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "东方幻术师-5", "怒雪咆哮-1", "占位符", "秘银巨石像");
        context.addToField(0, 0);
        CardInfo c怒雪咆哮 = context.addToField(1, 0);
        context.addToField(2, 1);
        CardInfo c秘银巨石像 = context.addToField(3, 1);
        context.startGame();

        // Enemy Round
        context.getStage().setActivePlayerNumber(1);
        context.proceedOneRound();
        Assert.assertTrue(c秘银巨石像.getStatus().containsStatus(CardStatusType.晕眩));
        Assert.assertEquals(660, 1703 - c怒雪咆哮.getHP());

        // Player Round
        random.addNextPicks(1); // 东方幻术师对秘银巨石像使用虚弱
        random.addNextPicks(1).addNextNumbers(0); // 东方幻术师对秘银巨石像使用迷魂成功
        context.proceedOneRound();

        // Enemy Round
        int hp怒雪咆哮ThisRound = c怒雪咆哮.getHP();
        int hpP1ThisRound = context.getPlayer(1).getHP();
        context.proceedOneRound();
        Assert.assertEquals(hp怒雪咆哮ThisRound, c怒雪咆哮.getHP());    // 秘银巨石像被晕眩，无法攻击怒雪咆哮
        Assert.assertFalse(c秘银巨石像.getStatus().containsStatus(CardStatusType.晕眩));
        Assert.assertEquals(330 /* 虚弱 */, hpP1ThisRound - context.getPlayer(1).getHP());
    }

    /**
     * 被迷魂且虚弱中的卡会以一半攻击力攻击自己英雄
     */
    @Test
    public void test虚弱_迷魂() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "东方幻术师-5", "秘银巨石像");
        CardInfo c东方幻术师 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        random.addNextPicks(0); // 东方幻术师对秘银巨石像使用虚弱
        random.addNextPicks(0).addNextNumbers(0); // 东方幻术师对秘银巨石像使用迷魂成功
        context.proceedOneRound();

        context.proceedOneRound();
        Assert.assertEquals(0, 1160 - c东方幻术师.getHP());
        Assert.assertEquals(330, 6390 - context.getPlayer(1).getHP());
    }
    
    /**
     * 被虚弱后，暴击仍然以基础攻击力计算加成
     */
    @Test
    public void test虚弱_暴击() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "东方幻术师-1", "金属巨龙-5");
        CardInfo c东方幻术师 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        random.addNextPicks(0); // 东方幻术师对金属巨龙使用虚弱
        context.proceedOneRound();

        random.addNextNumbers(0); // 金属巨龙暴击
        context.proceedOneRound();
        Assert.assertEquals(505 * 220 / 100 /* 暴击 */ - 505 / 2 /* 虚弱 */, 1056 - c东方幻术师.getHP());
    }
    
    /**
     * 虚弱不可被免疫
     */
    @Test
    public void test虚弱_免疫() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "东方幻术师-1", "金属巨龙");
        CardInfo c东方幻术师 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        random.addNextPicks(0); // 东方幻术师对金属巨龙使用虚弱
        context.proceedOneRound();

        random.addNextNumbers(1000); // 金属巨龙未暴击
        context.proceedOneRound();
        Assert.assertEquals(655 / 2 + 1 /* 虚弱无效 */, 1056 - c东方幻术师.getHP());
    }

    @Test
    public void test虚弱_多重() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "东方幻术师-1*2", "秘银巨石像");
        CardInfo c东方幻术师1 = context.addToField(0, 0);
        context.addToField(1, 0);
        CardInfo c秘银巨石像 = context.addToField(2, 1);
        context.startGame();

        random.addNextPicks(0); // 东方幻术师1对秘银巨石像使用虚弱
        random.addNextPicks(0); // 东方幻术师2对秘银巨石像使用虚弱
        context.proceedOneRound();

        context.proceedOneRound();
        Assert.assertEquals(165, 1056 - c东方幻术师1.getHP());
        Assert.assertEquals(660, c秘银巨石像.getCurrentAT());
    }
    
    @Test
    public void test战争怒吼_虚弱() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "东方幻术师", "血色骑士", "秘银巨石像*2");
        CardInfo c东方幻术师 = context.addToField(0, 0);
        CardInfo c血色骑士 = context.addToHand(1, 0).setSummonDelay(0);
        CardInfo c秘银巨石像1 = context.addToField(2, 1);
        CardInfo c秘银巨石像2 = context.addToField(3, 1);
        context.startGame();
        
        random.addNextPicks(0, 1); // 血色骑士的降临战争怒吼
        random.addNextPicks(0); // 东方幻术师虚弱秘银巨石像1
        random.addNextPicks(0).addNextNumbers(1000); // 东方幻术师迷魂失败
        random.addNextNumbers(1000); // 血色骑士暴击失败
        context.proceedOneRound();
        
        context.proceedOneRound();
        Assert.assertEquals(660 / 2 /* 虚弱 */ / 2 /* 战争怒吼 */, 1290 - c东方幻术师.getHP());
        Assert.assertEquals(660 / 2 /* 战争怒吼 */, 1210 - c血色骑士.getHP());
        Assert.assertEquals(660, c秘银巨石像1.getCurrentAT());
        Assert.assertEquals(660, c秘银巨石像2.getCurrentAT());
    }

    /**
     * 不屈状态下，连狙击都可以防住
     */
    @Test
    public void test不屈_狙击() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "残血王国小兵+不屈", "占位符", "秘银巨石像+狙击1");
        CardInfo c王国小兵 = context.addToField(0, 0);
        context.addToField(1, 0);
        context.addToField(2, 1);
        context.startGame();

        context.getStage().setActivePlayerNumber(1);
        context.proceedOneRound();
        Assert.assertEquals(2, context.getPlayer(0).getField().size());
        Assert.assertTrue(c王国小兵.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c王国小兵.getHP());
        
        context.proceedOneRound();
        Assert.assertFalse(c王国小兵.getStatus().containsStatus(CardStatusType.不屈));

        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertFalse(c王国小兵.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertTrue(c王国小兵.isDead());
    }

    /**
     * 不屈无法消除DEBUFF
     */
    @Test
    public void test不屈_中毒() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "残血王国小兵+不屈", "占位符+毒云10");
        CardInfo c王国小兵 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        context.getStage().setActivePlayerNumber(1);
        random.addNextPicks(0);     // 占位符的毒云
        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertTrue(c王国小兵.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertTrue(c王国小兵.getStatus().containsStatus(CardStatusType.中毒));
        Assert.assertEquals(1, c王国小兵.getHP());
        
        context.proceedOneRound();
        Assert.assertEquals(0, context.getPlayer(0).getField().size());
        Assert.assertFalse(c王国小兵.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertTrue(c王国小兵.isDead());
    }

    /**
     * 根据官方BUG，不屈能触发死契技能，真正死的时候还能再触发一次
     */
    @Test
    public void test不屈_死契技能() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "铸造大师+死契暴风雪1", "占位符+狙击1");
        CardInfo c铸造大师 = context.addToField(0, 0).setBasicHP(2);
        CardInfo c占位符 = context.addToField(1, 1);
        context.startGame();

        context.getStage().setActivePlayerNumber(1);
        random.addNextPicks(0).addNextNumbers(1000);    // 铸造大师发动死契暴风雪
        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertTrue(c铸造大师.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c铸造大师.getHP());
        Assert.assertEquals(20, 5000 - c占位符.getHP());

        context.proceedOneRound();

        random.addNextPicks(0).addNextNumbers(1000);    // 铸造大师真正死亡并再次发动死契暴风雪
        context.proceedOneRound();
        Assert.assertEquals(0, context.getPlayer(0).getField().size());
        Assert.assertFalse(c铸造大师.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertTrue(c铸造大师.isDead());
        Assert.assertEquals(560 /* 普通攻击 */ + 20 + 20 /* 两次死契暴风雪 */, 5000 - c占位符.getHP());
    }
    
    /**
     * 在己方回合发动的不屈，在下个敌方回合仍然有效
     */
    @Test
    public void test不屈_盾刺() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "见习圣骑+不屈", "秘银巨石像+盾刺10");
        CardInfo c见习圣骑 = context.addToField(0, 0).setBasicHP(2);
        context.addToField(1, 1);
        context.startGame();

        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertTrue(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c见习圣骑.getHP());

        // 本回合不屈依然有效
        random.addNextNumbers(1000); // 见习圣骑闪避失败
        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertTrue(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c见习圣骑.getHP());

        // 本回合不屈失效，被盾刺弹死
        context.proceedOneRound();
        Assert.assertEquals(0, context.getPlayer(0).getField().size());
        Assert.assertFalse(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertTrue(c见习圣骑.isDead());
    }

    
    /**
     * 在己方回合发动的不屈，在下个敌方回合仍然有效
     */
    @Test
    public void test不屈_连续盾刺() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "见习圣骑+不屈", "占位符", "秘银巨石像+盾刺10*2");
        CardInfo c见习圣骑 = context.addToField(0, 0).setBasicHP(2);
        context.addToField(1, 0);
        context.addToField(2, 1);
        context.addToField(3, 1);
        context.startGame();

        // 本回合盾刺被发动两次，第一次触发不屈，第二次被不屈挡下
        context.proceedOneRound();
        Assert.assertEquals(2, context.getPlayer(0).getField().size());
        Assert.assertTrue(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c见习圣骑.getHP());

        // 本回合不屈依然有效
        random.addNextNumbers(1000);   // 见习圣骑闪避失败
        context.proceedOneRound();
        Assert.assertEquals(2, context.getPlayer(0).getField().size());
        Assert.assertTrue(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c见习圣骑.getHP());

        // 本回合不屈失效，被盾刺弹死
        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertFalse(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertTrue(c见习圣骑.isDead());
    }

    /**
     * 不屈能免疫瘟疫和群体削弱
     */
    @Test
    public void test不屈_瘟疫_群体削弱() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "见习圣骑+不屈", "秘银巨石像", "占位符+瘟疫10", "占位符+群体削弱10");
        CardInfo c见习圣骑 = context.addToField(0, 0).setBasicHP(2);
        context.addToField(1, 1);
        context.addToField(2, 1);
        context.addToField(3, 1);
        context.startGame();

        context.getStage().setActivePlayerNumber(1);
        random.addNextNumbers(1000);   // 见习圣骑闪避失败
        random.addNextPicks(0, 0);     // 瘟疫和群体削弱
        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertTrue(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c见习圣骑.getHP());
        Assert.assertEquals(495 - 50 /* 瘟疫 */ - 50 /* 群体削弱 */, c见习圣骑.getCurrentAT());
    }

    /**
     * 不屈能抵御控制技能
     */
    @Test
    public void test不屈_迷惑_冰冻_麻痹_锁定() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "见习圣骑+不屈", "秘银巨石像", "占位符+迷魂10", "占位符+冰弹10", "占位符+落雷10", "占位符+陷阱1");
        CardInfo c见习圣骑 = context.addToField(0, 0).setBasicHP(2);
        CardInfo c秘银巨石像 = context.addToField(1, 1);
        context.addToField(2, 1);
        context.addToField(3, 1);
        context.addToField(4, 1);
        context.addToField(5, 1);
        context.startGame();

        context.getStage().setActivePlayerNumber(1);
        random.addNextNumbers(1000);    // 见习圣骑闪避失败
        random.addNextPicks(0, 0, 0, 0).addNextNumbers(0, 0, 0, 0);     // 迷魂冰弹落雷陷阱全中
        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertTrue(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c见习圣骑.getHP());

        context.proceedOneRound();
        // 见习圣骑并未受控制
        Assert.assertEquals(495, 1400 - c秘银巨石像.getHP());
    }

    /**
     * 发动不屈时不应该丢失种族之力
     */
    @Test
    public void test不屈_种族之力() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "铸造大师*2", "秘银巨石像");
        CardInfo c铸造大师1 = context.addToHand(0, 0).setSummonDelay(0);
        CardInfo c铸造大师2 = context.addToHand(1, 0).setSummonDelay(0);
        context.addToField(2, 1);
        context.startGame();

        context.proceedOneRound();
        Assert.assertEquals(460 + 150, c铸造大师1.getCurrentAT());
        Assert.assertEquals(460 + 150, c铸造大师2.getCurrentAT());

        c铸造大师1.setBasicHP(2);
        c铸造大师2.setBasicHP(2);
        context.proceedOneRound();
        Assert.assertTrue(c铸造大师1.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertFalse(c铸造大师2.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c铸造大师1.getHP());
        Assert.assertEquals(2, c铸造大师2.getHP());
        Assert.assertEquals(460 + 150, c铸造大师1.getCurrentAT());
        Assert.assertEquals(460 + 150, c铸造大师2.getCurrentAT());

        context.proceedOneRound();

        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertEquals(460, c铸造大师2.getCurrentAT());
    }

    /**
     * 不屈无法抵挡摧毁
     */
    @Test
    public void test不屈_摧毁() {
        SkillTestContext context = SkillValidationTestSuite.prepare(50, 50, "见习圣骑+不屈", "占位符+盾刺10", "独眼巨人");
        CardInfo c见习圣骑 = context.addToField(0, 0);
        context.addToField(1, 1);
        context.startGame();

        c见习圣骑.setBasicHP(2);
        context.proceedOneRound();
        // 见习圣骑被雷兽盾刺弹死，发动不屈
        Assert.assertTrue(c见习圣骑.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c见习圣骑.getHP());

        context.addToHand(2, 1).setSummonDelay(0);
        random.addNextPicks(0);   // 独眼巨人的降临群体削弱
        random.addNextPicks(0);   // 独眼巨人的降临摧毁
        context.proceedOneRound();
        // 见习圣骑被摧毁，不屈无法抵挡摧毁
        Assert.assertEquals(0, context.getPlayer(0).getField().size());
    }

    /**
     * 不屈无法抵挡送还
     */
    @Test
    public void test不屈_送还() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "秘银巨石像", "残血王国小兵+不屈", "占位符+盾刺10", "占位符+送还");
        context.addToField(0, 0);
        CardInfo c王国小兵 = context.addToField(1, 0);
        context.addToField(2, 1);
        context.addToField(3, 1);
        context.startGame();

        c王国小兵.setBasicHP(2);
        context.proceedOneRound();
        // 王国小兵被雷兽盾刺弹死，发动不屈
        Assert.assertTrue(c王国小兵.getStatus().containsStatus(CardStatusType.不屈));
        Assert.assertEquals(1, c王国小兵.getHP());

        context.proceedOneRound();
        // 王国小兵被送还，不屈无法抵挡送还
        Assert.assertEquals(1, context.getPlayer(0).getField().size());
        Assert.assertEquals(1, context.getPlayer(0).getDeck().size());
    }

    /**
     * 不屈状态的卡不会被狙击
     */
    @Test
    public void test不屈_二重狙击() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "秘银巨石像", "占位符+二重狙击10", "残血王国小兵+不屈", "占位符+盾刺10", "占位符+送还");
        context.addToField(0, 0);
        context.addToField(1, 0);
        CardInfo c王国小兵 = context.addToField(2, 1);
        CardInfo c占位符2 = context.addToField(3, 1);
        CardInfo c占位符3 = context.addToField(4, 1);
        context.startGame();

        context.proceedOneRound();
        Assert.assertEquals(3, context.getPlayer(1).getField().size());
        Assert.assertEquals(1, c王国小兵.getHP());
        // 二重狙击打在两个后方占位符上
        Assert.assertEquals(250, 5000 - c占位符2.getHP());
        Assert.assertEquals(250, 5000 - c占位符3.getHP());
    }

    /**
     * 不屈状态的卡不会被治疗
     */
    @Test
    public void test不屈_治疗() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "秘银巨石像", "金属巨龙+不屈", "占位符+治疗1", "占位符+盾刺10");
        CardInfo c秘银巨石像 = context.addToField(0, 0);
        CardInfo c金属巨龙 = context.addToField(1, 0).setBasicHP(2);
        CardInfo c占位符1 = context.addToField(2, 0);
        context.addToField(3, 1);
        context.startGame();

        context.proceedOneRound();
        Assert.assertEquals(3, context.getPlayer(0).getField().size());
        // 不屈的卡无法被治疗
        Assert.assertEquals(1, c金属巨龙.getHP());
        // 二重狙击打在两个后方占位符上
        Assert.assertEquals(200 /* 盾刺 */ - 25 /* 治疗1 */, 1400 - c秘银巨石像.getHP());
        Assert.assertEquals(0, 5000 - c占位符1.getHP());
    }

    /**
     * 不屈状态的卡不会被甘霖
     */
    @Test
    public void test不屈_甘霖() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "秘银巨石像", "金属巨龙+不屈", "占位符+甘霖1", "占位符+盾刺10");
        CardInfo c秘银巨石像 = context.addToField(0, 0);
        CardInfo c金属巨龙 = context.addToField(1, 0).setBasicHP(2);
        CardInfo c占位符1 = context.addToField(2, 0);
        context.addToField(3, 1);
        context.startGame();

        context.proceedOneRound();
        Assert.assertEquals(3, context.getPlayer(0).getField().size());
        // 不屈的卡无法被甘霖
        Assert.assertEquals(1, c金属巨龙.getHP());
        // 二重狙击打在两个后方占位符上
        Assert.assertEquals(200 /* 盾刺 */ - 25 /* 治疗1 */, 1400 - c秘银巨石像.getHP());
        Assert.assertEquals(0, 5000 - c占位符1.getHP());
    }

    /**
     * 不屈状态的卡可以吸血
     */
    @Test
    public void test不屈_吸血() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "秘银巨石像", "熊人武士+不屈", "占位符+盾刺10", "占位符");
        CardInfo c秘银巨石像 = context.addToField(0, 0);
        CardInfo c熊人武士 = context.addToField(1, 0).setBasicHP(2);
        context.addToField(2, 1);
        context.addToField(3, 1);
        context.startGame();

        context.proceedOneRound();
        Assert.assertEquals(2, context.getPlayer(0).getField().size());
        // 不屈的卡可以吸血
        Assert.assertEquals(1 + 1170 /* 吸血 */, c熊人武士.getHP());
        // 二重狙击打在两个后方占位符上
        Assert.assertEquals(200 /* 盾刺 */, 1400 - c秘银巨石像.getHP());
    }

    /**
     * 不屈状态的卡会被虚弱
     */
    @Test
    public void test不屈_虚弱() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "秘银巨石像", "占位符+虚弱", "秘银巨石像+不屈");
        CardInfo c秘银巨石像1 = context.addToField(0, 0);
        context.addToField(1, 0);
        CardInfo c秘银巨石像2 = context.addToField(2, 1).setBasicHP(2);
        context.startGame();

        random.addNextPicks(0); // 占位符虚弱对方秘银巨石像
        context.proceedOneRound();
        Assert.assertTrue(c秘银巨石像2.getStatus().containsStatus(CardStatusType.不屈));
        // 不屈的卡仍然会被弱化
        Assert.assertTrue(c秘银巨石像2.getStatus().containsStatus(CardStatusType.弱化));

        context.proceedOneRound();
        Assert.assertEquals(810 / 2, 1400 - c秘银巨石像1.getHP());
    }

    /**
     * 不屈状态的卡也会被献祭
     */
    @Test
    public void test不屈_献祭() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "秘银巨石像*2", "铸造大师+死契复活", "秘银巨石像+献祭1");
        CardInfo c秘银巨石像1 = context.addToField(0, 0);
        context.addToField(1, 0).setBasicHP(2);
        CardInfo c铸造大师 = context.addToField(2, 1).setBasicHP(2);
        context.addToGrave(3, 1);
        context.startGame();

        random.addNextPicks(0); // 复活秘银巨石像
        random.addNextPicks(0); // 献祭铸造大师
        context.proceedOneRound();
        Assert.assertTrue(c铸造大师.isDead());

        context.proceedOneRound();
        Assert.assertEquals(1, context.getPlayer(1).getField().size());
        Assert.assertEquals(810 * 130 / 100, 1400 - c秘银巨石像1.getHP());
    }

    @Test
    public void test森林沐浴_基本() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "赤面天狗", "占位符");
        context.addToHand(0, 0).setSummonDelay(0);
        CardInfo c占位符 = context.addToField(1, 1);
        context.startGame();
        context.proceedOneRound();
        Assert.assertEquals(745 * 220 / 100 /* 污染7 */, 5000 - c占位符.getHP());
    }

    /**
     * 免疫脱困无法抵挡森林沐浴
     */
    @Test
    public void test森林沐浴_免疫_脱困() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "占位符+降临森林沐浴", "占位符+免疫", "占位符+脱困");
        context.addToHand(0, 0).setSummonDelay(0);
        CardInfo c占位符2 = context.addToField(1, 1);
        CardInfo c占位符3 = context.addToField(2, 1);
        context.startGame();
        
        context.proceedOneRound();
        Assert.assertEquals(Race.FOREST, c占位符2.getRace());
        Assert.assertEquals(Race.FOREST, c占位符3.getRace());
    }

    /**
     * 森林沐浴的效果是在卡牌结束行动时解除的
     */
    @Test
    public void test森林沐浴_死契复活() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "占位符+降临森林沐浴", "占位符", "占位符+死契复活", "秘银巨石像*3");
        context.addToGrave(0, 0);
        context.addToField(1, 0);
        context.addToField(2, 0).setBasicHP(2);
        CardInfo c秘银巨石像1 = context.addToField(3, 1);
        CardInfo c秘银巨石像2 = context.addToField(4, 1);
        CardInfo c秘银巨石像3 = context.addToField(5, 1);
        context.startGame();
        context.getStage().setActivePlayerNumber(1);
        
        random.addNextPicks(1); // 死契复活占位符+降临森林沐浴
        context.proceedOneRound();
        Assert.assertEquals(Race.FOREST, c秘银巨石像1.getRace());
        // 秘银巨石像2和3结束行动的同时解除沐浴状态
        Assert.assertEquals(Race.KINGDOM, c秘银巨石像2.getRace());
        Assert.assertEquals(Race.KINGDOM, c秘银巨石像3.getRace());
        
        context.proceedOneRound();
        context.proceedOneRound();
        Assert.assertEquals(Race.KINGDOM, c秘银巨石像1.getRace());
        Assert.assertEquals(Race.KINGDOM, c秘银巨石像2.getRace());
        Assert.assertEquals(Race.KINGDOM, c秘银巨石像3.getRace());
    }
    
    /**
     * 被森林沐浴改变成森林的卡无法享受森林之力的BUFF
     */
    @Test
    public void test森林沐浴_森林之力_主动() {
        SkillTestContext context = SkillValidationTestSuite.prepare(
            50, 50, "占位符+降临森林沐浴", "占位符+降临摧毁", "秘银巨石像", "金属巨龙", "森林女神+森林之力1");
        context.addToHand(0, 0).setSummonDelay(0);
        context.addToHand(1, 0).setSummonDelay(2);
        CardInfo c秘银巨石像 = context.addToField(2, 1);
        CardInfo c金属巨龙 = context.addToField(3, 1);
        context.addToHand(4, 1).setSummonDelay(0);
        context.startGame();
        
        context.proceedOneRound();

        context.proceedOneRound();
        // 秘银巨石像不能因为森林沐浴享受森林守护和森林之力
        Assert.assertEquals(660, c秘银巨石像.getCurrentAT());
        Assert.assertEquals(1400, c秘银巨石像.getHP());
        Assert.assertEquals(655 + 25 /* 森林之力1 */, c金属巨龙.getCurrentAT());
        Assert.assertEquals(1710 + 250 /* 森林之力1 */, c金属巨龙.getHP());
        
        random.addNextPicks(2); /* 摧毁森林女神 */
        context.proceedOneRound();
        Assert.assertEquals(660, c秘银巨石像.getCurrentAT());
        Assert.assertEquals(1400, c秘银巨石像.getHP());
        Assert.assertEquals(655, c金属巨龙.getCurrentAT());
        Assert.assertEquals(1710, c金属巨龙.getHP());
    }
}
