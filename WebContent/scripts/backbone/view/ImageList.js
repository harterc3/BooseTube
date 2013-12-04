App.View.ImageList = Backbone.View.extend({
	template: window.Templates.ImageList,
	
    initialize: function() {
    	this.setElement($('#imageList')[0]);
        this.render();
    },

    render: function() {
        if (!this.template) return;
        
        this.$el.html(this.template);
        
        var self = this;
        _.each(this.model, function(item) {
        	self.$el.append(new App.View.Image({model:item.attributes}).el);
        });
    },
    
    addImage: function(model) {
    	if (!model) return;
    	this.$el.append(model.el);
    }
});