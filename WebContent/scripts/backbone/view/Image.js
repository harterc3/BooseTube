App.View.Image = Backbone.View.extend({
	template: window.Templates.Image,
	
    initialize: function() {
    	//this.setElement($('#imageList')[0]);
        this.render();
    },

    render: function() {
        if ( this.template ) {
            this.$el.html( this.template( this.model ));
        }
    }
});