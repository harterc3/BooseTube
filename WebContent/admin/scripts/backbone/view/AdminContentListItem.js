App.View.AdminContentListItem = Backbone.View.extend({
	template: window.Templates.AdminContentListItem,
	
	/*
	events: function() {
        return navigator.userAgent.match(/mobile/i) ? 
        {
            "touchstart .thumbAnchor" : "setModal"
        } :
        {
            "click .thumbAnchor" : "setModal"
        };
    },
    */
	
    initialize: function(params) {
    	this.controller = params.controller;
        this.render();
    },

    render: function() {
        if ( !this.template ) return;
        this.$el.html( this.template( this.model ));
    }
});