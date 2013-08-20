package uk.ac.bham.cs.sdsts.common;


import java.util.ArrayList;

public class ModelManager {
	private static ModelManager modelManager;
	private String font = "Monaco";
	private String font_size = "12";
	
	public String getFont_size() {
		return font_size;
	}

	public void setFont_size(String font_size) {
		this.font_size = font_size;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}
	private ArrayList<Model> models = new ArrayList<Model>();
	
	public static ModelManager getInstance(){
		if(modelManager == null){
			modelManager = new ModelManager();
		}
		return modelManager;
	}
	
	public void AddModel(Model model){
		for (Model model2 : models) {
			if(model2.getFilename().equals(model.getFilename()))
				return;
		}
		models.add(model);
	}
	public Model getModel(String path){
		for (Model model : models) {
			if(model.getFilepath().equals(path))
				return model;
		}
		return null;
	}
	public ArrayList<Model> getModels(){
		return models;
	}
	public void removeModel(Model model){
		this.models.remove(model);
	}
	
}
