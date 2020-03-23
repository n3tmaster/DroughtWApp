
function chartExtender() {
    this.cfg.animate = true;
    this.cfg.animateReplot = true;
    this.cfg.grid = {
        background : '#FFFFFF',
    }
    this.cfg.canvasOverlay = {
        show: true,
        objects: [{horizontalLine: {
                name: 'Extremely',
                y: -2,
                lineWidth: 2,
                color: 'rgb(155, 64, 58)',
                shadow: false,
                lineCap: 'butt',
                xOffset: 0
            }},
            {horizontalLine: {
                    name: 'Severely',
                    y: -1.5,
                    lineWidth: 2,
                    color: 'rgb(242, 139, 104)',
                    shadow: false,
                    lineCap: 'butt',
                    xOffset: 0
                }},
            {horizontalLine: {
                    name: 'Dry: Moderately',
                    y: -1,
                    lineWidth: 2,
                    color: 'rgb(239, 212, 97)',
                    shadow: false,
                    lineCap: 'butt',
                    xOffset: 0
                }},
            {horizontalLine: {
                    name: 'Moderately',
                    y: 1,
                    lineWidth: 2,
                    color: 'rgb(150, 231, 130)',
                    shadow: false,
                    lineCap: 'butt',
                    xOffset: 0
                }},
            {horizontalLine: {
                    name: 'Severely',
                    y: 1.5,
                    lineWidth: 2,
                    color: 'rgb(115, 190, 93)',
                    shadow: false,
                    lineCap: 'butt',
                    xOffset: 0
                }},
            {horizontalLine: {
                    name: 'Wet: Extremely',
                    y: 2,
                    lineWidth: 2,
                    color: 'rgb(86, 130, 75)',
                    shadow: false,
                    lineCap: 'butt',
                    xOffset: 0
                }},

        ]
    };

}


function chartExtenderVCI() {
    this.cfg.animate = true;
    this.cfg.animateReplot = true;
    this.cfg.grid = {
        background : '#FFFFFF',
    }
    this.cfg.canvasOverlay = {
        show: true,
        objects: [{horizontalLine: {
                name: 'warning',
                y: 40,
                lineWidth: 2,
                color: 'rgb(155, 64, 58)',
                shadow: false,
                lineCap: 'butt',
                xOffset: 0
            }}
        ]
    };

}

/*
function exportChart() {

    $('#exportChart').empty().append(PF('seriesChart').exportAsImage());

    PF('dlgExport').show();
}
*/