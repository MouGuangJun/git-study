package proxy;

public class GamePlayer implements IGamePlayer{
	String name;
	
	public GamePlayer() {
		
	}
	
	public GamePlayer(String name) {
		this.name = name;
	}
	
	@Override
	public int login(String name) {
		System.out.println(name + "已登录游戏！");
		
		return 0;
	}

	@Override
	public void killBoss() {
		System.out.println(name + "正在刷怪！");
	}

	@Override
	public void upgrade() {
		System.out.println(name + "恭喜升级！");
	}
	
}
