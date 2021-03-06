package cfvbaibai.cardfantasy.test.func;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cfvbaibai.cardfantasy.engine.BattleEngine;
import cfvbaibai.cardfantasy.game.DeckBuilder;
import cfvbaibai.cardfantasy.game.DeckStartupInfo;
import cfvbaibai.cardfantasy.test.TestGameBuilder;

@RunWith(Suite.class)
@SuiteClasses({
    RacialBufferTest.class,
    AttackBuffTest.class,
    SpecialStatusTest.class,
    DelayTest.class,
    CounterAttackTest.class,
    DefenseTest.class,
    MultiAttackTest.class,
    DeathSkillTest.class,
    SummonSkillTest.class,
    SkillSequenceTest.class,
    HealingTest.class,
    RuneActivationTest.class,
    SummonTest.class,
    SpecificCardTest.class,
    SpecificRuneTest.class,
    SnipeTest.class,
    ReviveTest.class
})
public class SkillValidationTestSuite {
    public static SkillTestContext prepare(int playerALevel, int playerBLevel, String ... cards) {
        BattleEngine engine = TestGameBuilder.buildEmptyGameForTest(50, 50);
        DeckStartupInfo dsi = DeckBuilder.build(cards);
        SkillTestContext context = new SkillTestContext();
        context.setEngine(engine);
        context.setDsi(dsi);
        return context;
    }
}
