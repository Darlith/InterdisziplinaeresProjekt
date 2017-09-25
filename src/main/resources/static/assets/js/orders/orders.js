$(function() {
    var endPoints = {
        getOrders: 'partials/orders',
        addSampleOrders: 'partials/addSampleOrders',
        addDemoOrders: 'partials/addCsvOrders'
    };

    // load already processed orders
    getOrders();

    // Register click handlers
    $('#uploadCsvBtn').click(handleCsvSubmit);
    $('#uploadDemoBtn').click(handleDemoSubmit);
    $('#deleteOrderData').click(handleDeleteOrderDataSubmit);

    function handleCsvSubmit(e) {
        var files = $('#csvInput')[0].files;
        // Browser Kompabilität überprüfen
        if (!window.File || !window.FileReader || !window.FileList || !window.Blob || !files) {
            showNotification('Ihr Browser unterstützt keine Dateiuploads. Bitte Browser wechseln', 'danger');
            return;
        }
        // Prüfen ob Datei ausgewählt
        if (!files[0]) {
            showNotification('Keine Datei ausgewählt', 'danger');
            return;
        }
        var file = files[0];
        var reader = new FileReader();
        reader.readAsText(file);
        reader.onload = function (event) {
            var csvData = event.target.result;
            var data = $.csv.toObjects(csvData);
            if (data && data.length > 0) {
                handleCsvUpload(data);
            } else {
                showNotification('Keine Bestellungen in Datei gefunden', 'danger');
            }
        };
        reader.onerror = function() {
          showNotification('Fehler beim Lesen der CSV-Datei', 'danger');
        };
    }

    function handleCsvUpload(orders) {
        showNotification("Die Bestellungen werden verabeitet");
        $('#uploadCsvBtn').html('Verarbeite Daten...');
        var str = JSON.stringify(orders);
        str = str.replace(/Bestellzeitpunkt/g, 'orderTime');
        str = str.replace(/Gewicht/g, 'weight');
        str = str.replace(/Zielort/g, 'location');
        orders = JSON.parse(str);

        $.ajax({
            type: 'POST',
            url: endPoints.addDemoOrders,
            data: JSON.stringify(orders),
            contentType: "application/json",
            success: function(response) {
                $('#orderList').html(response);
                showNotification("Die Bestellungen wurden erfolgreich verarbeitet");
                $('#uploadCsvBtn').html('Abschicken');
            },
            error: function() {
                showNotification('Es ist ein Fehler aufgetreten. Bitte CSV-Struktur überprüfen', 'danger');
                $('#uploadCsvBtn').html('Abschicken');
            }
        });

        console.log(orders);
    }

    function handleDeleteOrderDataSubmit() {
        var uri = '/orders/delete';
        $.ajax({
            type: 'DELETE',
            url: uri,
            success: function() {
                showNotification("Die Bestellungen wurden gelöscht", 'danger');
                getOrders();
            }
        })
    }

    function handleDemoSubmit(e) {
        showNotification("Die Bestellungen werden verabeitet");
        $('#uploadDemoBtn').html('Verarbeite Daten...');
        $('#orderList').load(endPoints.addSampleOrders, function() {
            showNotification("Die Bestellungen wurden erfolgreich verarbeitet");
            $('#uploadDemoBtn').html('Abschicken');
        });
    }

    function getOrders(callback) {
        $('#orderList').load(endPoints.getOrders, callback);
    }

    function showNotification(msg, type) {
        type = type || 'success';
        $.notify({
            icon: "notifications",
            message: msg
        },{
            type: type,
            timer: 3000,
            placement: {
                from: 'top',
                align: 'right'
            }
        });

    }
});
