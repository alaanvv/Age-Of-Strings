import java.util.ArrayList;

public class Empire {
  private int food = 10;
  private int wood = 50;
  private int iron = 0;
  private int gold = 10;
  private int population = 3;

  private LumberCamp lumber_camp = new LumberCamp();
  private ArrayList<Farm> farms  = new ArrayList<>();
  private ArrayList<Mine> mines  = new ArrayList<>();
  
  // ---

  public int build_farm() {
    if (wood < 5 || gold < 2) return 0;
    wood -= 5;
    gold -= 2;

    farms.add(new Farm(farms.size()));
    return 1;
  }
  
  public int build_mine() {
    if (wood < 15 || gold < 5) return 0;
    wood -= 15;
    gold -= 5;

    mines.add(new Mine(mines.size()));
    return 1;
  }
}
