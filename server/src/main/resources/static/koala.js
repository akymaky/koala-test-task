function main() {
    const h1 = document.querySelector("h1")
    const ul = document.querySelector("ul")

    const ws = new WebSocket("ws://localhost:8080/breakpoints")

    ws.onmessage = (e) => {
        const [event, message] = e.data.split("\n")

        console.log(`${event}: ${message}`)

        if (event === "add") {
            const [id, text] = message.split(";")

            const li = document.createElement("li")
            li.dataset.id = id
            li.textContent = text

            ul.appendChild(li)

            h1.textContent = `Breakpoints [${ul.childNodes.length}]`
        }

        if (event === "remove") {
            document.querySelector(`li[data-id='${message}']`).remove()
            h1.textContent = `Breakpoints [${ul.childNodes.length}]`
        }
    }
}

main()