document.addEventListener('DOMContentLoaded', () => {
    const meterReadingForm = document.getElementById('meterReadingForm');
    const netDifferenceForm = document.getElementById('netDifferenceForm');
    const netDiffResultDiv = document.getElementById('netDiffResult');
    const submitReading = document.getElementById('submitReading');

    meterReadingForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(meterReadingForm);
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });

        const response = await fetch('/api/energy/meterReading', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.text();
        if(result.slice(0,24) === "New energy reading added") {
            submitReading.innerHTML = `<p>${result.slice(0,24)}</p>`;
        } else {
            submitReading.innerHTML = `<p>${result}</p>`;
        }

    });

    netDifferenceForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(netDifferenceForm);
        const params = new URLSearchParams(formData).toString();

        const response = await fetch(`/api/energy/netDifference?${params}`);
        const result = await response.json();

        if (result.netEnergyDifference === undefined) {
            netDiffResultDiv.innerHTML = `<p>Please select the month</p>`;
        } else {
            netDiffResultDiv.innerHTML = `<p>${result.netEnergyDifference}</p>`;
        }


    });
});
