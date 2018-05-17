snake_thanks = {

    init: function () {
        this.redirectAfter();
    },

    redirectAfter: function () {
        window.addEventListener('load', function () {
            const timer = ms => new Promise(resolve => setTimeout(resolve, ms));
            timer(2000).then(() => {
                document.location.href = "/";
            })
        })
    }
};

snake_thanks.init();