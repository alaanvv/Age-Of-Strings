import java.util.ArrayList;

public class Empire {
  private int food = 10;
  private int wood = 50;
  private int iron = 0;
  private int gold = 10;
  private int population = 3;

  private LumberCamp lumber_camp = new LumberCamp();
  private ArrayList<Mine> mines  = new ArrayList<>();
  private ArrayList<Farm> farms  = new ArrayList<>();
}
