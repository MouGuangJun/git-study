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
		System.out.println(name + "�ѵ�¼��Ϸ��");
		
		return 0;
	}

	@Override
	public void killBoss() {
		System.out.println(name + "����ˢ�֣�");
	}

	@Override
	public void upgrade() {
		System.out.println(name + "��ϲ������");
	}
	
}
