App.View.AdminContentList = Backbone.View.extend({
	
    initialize: function(params) {
    	this.controller = params.controller;
    	this.setElement($('#contentList')[0]);
        this.render();
    },

    render: function() {
        var self = this;
        _.each(this.model, function(item) {
        	self.$el.append(new App.View.AdminContentListItem({model:item.attributes,controller:self}).el);
        });
    }
});