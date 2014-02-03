App.View.Modal = Backbone.View.extend({
	imageTemplate: window.Templates.ModalImage,
	videoTemplate: window.Templates.ModalVideo,
	audioTemplate: window.Templates.ModalAudio,
	
    initialize: function(params) {
    	this.controller = params.controller;
    	this.setElement($('#contentModal')[0]);
    },
    
    updateModal: function(model) {
    	var tpl = this.imageTemplate;
    	if (model.type == "video") tpl = this.videoTemplate;
    	else if (model.type == "audio") tpl = this.audioTemplate;
    	
    	this.$el.find(".modal-body").html(tpl(model));
    	
    	this.$el.find("#modalImage").attr("src", model.filepath);
    	this.$el.find("#modalTitle").html(model.name);
    	
    	history.pushState({},"","detail.html?id=" + model._id);
    	this.$el.on('hidden.bs.modal', function () {
    		history.back();
		});
    	
    	if (model.type == "video") {
    		this.$el.find('video').mediaelementplayer();
    	} else if (model.type == "audio") {
    		this.$el.find('audio').mediaelementplayer();
    	}
    }
});