App.View.Modal = Backbone.View.extend({
	imageTemplate: window.Templates.ModalImage,
	videoTemplate: window.Templates.ModalVideo,
	
    initialize: function(params) {
    	this.controller = params.controller;
    	this.setElement($('#contentModal')[0]);
    },
    
    updateModal: function(model) {
    	var tpl = this.imageTemplate;
    	if (model.type == "video") tpl = this.videoTemplate;
    	this.$el.find(".modal-body").html(tpl(model));
    	
    	this.$el.find("#modalImage").attr("src", model.filepath);
    	this.$el.find("#modalTitle").html(model.name);
    	
    	history.pushState({},"","image.html?imageId=" + this.model._id);
    	this.$el.on('hidden.bs.modal', function () {
    		history.back();
		});
    }
});