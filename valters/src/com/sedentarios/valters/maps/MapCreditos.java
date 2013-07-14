package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;

public class MapCreditos extends ValtersMap {
	
	private Array<Credito> creditos;
	private Array<Imagem> imagens;
	
	public MapCreditos() {
		super(0, 0);
	}

	@Override
	public void create() {
		assetManager.load("assets/creditos/libgdx.png", Texture.class);
		assetManager.load("assets/creditos/java.png", Texture.class);
		assetManager.load("assets/creditos/eclipse.png", Texture.class);
		assetManager.load("assets/creditos/sublime.png", Texture.class);
		assetManager.load("assets/creditos/paintnet.png", Texture.class);
		assetManager.load("assets/creditos/photoshop.png", Texture.class);
		assetManager.load("assets/creditos/flash.png", Texture.class);
		assetManager.load("assets/creditos/illustrator.png", Texture.class);
		assetManager.load("assets/creditos/blender.png", Texture.class);
		assetManager.load("assets/creditos/audacity.png", Texture.class);
		assetManager.load("assets/creditos/tiled.png", Texture.class);
		assetManager.load("assets/creditos/tp.png", Texture.class);
		
		
	}

	@Override
	public void createObjects() {
		creditos = new Array<MapCreditos.Credito>();
		imagens = new Array<MapCreditos.Imagem>();
		int y = 0;
		int step = 400;
		creditos.add(new Credito(y+=step, "Programeiro & Análise dos problemas de lógica booleana recursiva", "Guilherme Hernandez"));
		creditos.add(new Credito(y+=step, "Desenheiro &  Engenharia vetorial da subdivisão de polígonos de ponto flutuante", "Gustavo Virgilio"));
		creditos.add(new Credito(y+=step, "Mestre da Obra & Engenheiro Responsável pela análise e realização de áudio", "Guilherme Assis"));
		creditos.add(new Credito(y+=step, "Beta Testeiro, Hosteiro, Técnico renderizador", "Fernando Fukamizu"));
		creditos.add(new Credito(y+=step, "FEZ P**** NENHUMA & Detentor dos poderes de invisibilidade", "Kaue Winchester Lopes"));
		creditos.add(new Credito(y+=step + 200, "", "PROGRAMAS UTILIZADOS"));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/libgdx.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/java.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 250, assetManager.get("assets/creditos/eclipse.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/sublime.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/paintnet.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/photoshop.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/flash.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/illustrator.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/blender.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/audacity.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/tiled.png", Texture.class), 1f));
		imagens.add(new Imagem(y+=step + 200, assetManager.get("assets/creditos/tp.png", Texture.class), 1f));
	}
	
	@Override
	protected void inBatchRender(com.badlogic.gdx.graphics.OrthographicCamera camera, com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
		ValtersGame.setCamPosition(720, 0);
		float speed = getRuntime() > 54f?150f:50f;
		for(Credito c : creditos){
			c.y -= Gdx.graphics.getDeltaTime() * speed;
			ValtersGame.font18.draw(batch, c.titulo, c.x - c.widthTitulo / 2, c.y);
			ValtersGame.font.draw(batch, c.nome, c.x - c.widthNome / 2, c.y + 64);
		}
		
		for(Imagem i : imagens){
			i.y -= Gdx.graphics.getDeltaTime() * speed;
			batch.draw(i.texture, i.x - i.texture.getWidth() / 2, i.y, i.texture.getWidth() * i.scale, i.texture.getHeight() * i.scale);
		}
		
		if(getRuntime() >= 120f){
			ValtersGame.changeMap(MapMenu.class);
		}
		
	};
	
	private class Imagem{
		public float x, y;
		public Texture texture;
		public float scale;
		public Imagem(float y, Texture texture, float scale){
			this.texture = texture;
			this.x = Gdx.graphics.getWidth() / 2;
			this.y = y;
			this.scale = scale;
		}
	}
	
	private class Credito{
		public float x, y;
		public float widthTitulo;
		public float widthNome;
		public String titulo;
		public String nome;
		public Credito(float y, String titulo, String nome){
			widthTitulo = ValtersGame.font18.getBounds(titulo).width;
			widthNome = ValtersGame.font.getBounds(nome).width;
			this.x = Gdx.graphics.getWidth() / 2;
			this.y = y;
			this.titulo = titulo;
			this.nome = nome;
		}
	}

}

