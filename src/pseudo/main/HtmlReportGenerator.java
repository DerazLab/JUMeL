package pseudo.main;

import java.io.FileWriter;
import java.io.IOException;

public class HtmlReportGenerator {
    public static void generarReporte(String rutaSalida, String codigoMermaid, String origen) {
        // Escapamos comillas o caracteres especiales del código mermaid si se va a incrustar en JavaScript o en el bloque de código
        String codigoMermaidEscapado = codigoMermaid
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");

        // ⋆˖⁺‧₊☽⛥Plantilla HTML con diseño premium, responsivo y lado a lado⛥☾₊‧⁺˖⋆ //
        String htmlTemplate = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Visualizador UML - Premium</title>\n" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;800&family=Fira+Code:wght@400;500&display=swap\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        :root {\n" +
                "            --primary: #ea580c;\n" +
                "            --primary-hover: #c2410c;\n" +
                "            --bg: #f8fafc;\n" +
                "            --card-bg: rgba(255, 255, 255, 0.9);\n" +
                "            --text: #0f172a;\n" +
                "            --text-secondary: #475569;\n" +
                "            --border: rgba(226, 232, 240, 0.6);\n" +
                "            --code-bg: #f1f5f9;\n" +
                "        }\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            font-family: 'Outfit', sans-serif;\n" +
                "            background-color: var(--bg);\n" +
                "            background-image: radial-gradient(circle at top right, rgba(234, 88, 12, 0.06), transparent 40%), \n" +
                "                              radial-gradient(circle at bottom left, rgba(249, 115, 22, 0.04), transparent 40%);\n" +
                "            color: var(--text);\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            align-items: center;\n" +
                "            min-height: 100vh;\n" +
                "            padding: 2rem;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 1400px;\n" +
                "            width: 100%;\n" +
                "            background: var(--card-bg);\n" +
                "            backdrop-filter: blur(16px);\n" +
                "            -webkit-backdrop-filter: blur(16px);\n" +
                "            border: 0.5px solid var(--border);\n" +
                "            border-radius: 0px;\n" +
                "            padding: 2.5rem;\n" +
                "            box-shadow: 0 20px 25px -5px rgba(15, 23, 42, 0.02), 0 10px 10px -5px rgba(15, 23, 42, 0.02);\n" +
                "        }\n" +
                "        header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 2.5rem;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            margin: 0;\n" +
                "            font-size: 2.5rem;\n" +
                "            font-weight: 800;\n" +
                "            background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);\n" +
                "            -webkit-background-clip: text;\n" +
                "            -webkit-text-fill-color: transparent;\n" +
                "            letter-spacing: -0.025em;\n" +
                "        }\n" +
                "        .subtitle {\n" +
                "            color: var(--text-secondary);\n" +
                "            margin-top: 0.5rem;\n" +
                "            font-size: 1.1rem;\n" +
                "        }\n" +
                "        .content-grid {\n" +
                "            display: grid;\n" +
                "            grid-template-columns: 1fr 1fr;\n" +
                "            gap: 2rem;\n" +
                "            margin-bottom: 2rem;\n" +
                "        }\n" +
                "        @media (max-width: 1024px) {\n" +
                "            .content-grid {\n" +
                "                grid-template-columns: 1fr;\n" +
                "            }\n" +
                "        }\n" +
                "        .panel {\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            background: #ffffff;\n" +
                "            border: 0.5px solid var(--border);\n" +
                "            border-radius: 0px;\n" +
                "            overflow: hidden;\n" +
                "            height: 600px;\n" +
                "        }\n" +
                "        .panel-header {\n" +
                "            background: #f8fafc;\n" +
                "            padding: 1rem 1.5rem;\n" +
                "            border-bottom: 0.5px solid var(--border);\n" +
                "            display: flex;\n" +
                "            justify-content: space-between;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        .panel-title {\n" +
                "            margin: 0;\n" +
                "            font-size: 1.1rem;\n" +
                "            font-weight: 600;\n" +
                "            color: #1e293b;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            gap: 0.5rem;\n" +
                "        }\n" +
                "        .panel-body {\n" +
                "            flex: 1;\n" +
                "            padding: 1.5rem;\n" +
                "            overflow: auto;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        /* Lado del UML */\n" +
                "        .uml-body {\n" +
                "            background: #ffffff;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            border-radius: 0px;\n" +
                "            position: relative;\n" +
                "            overflow: hidden;\n" +
                "            cursor: grab;\n" +
                "            user-select: none;\n" +
                "        }\n" +
                "        .uml-body:active {\n" +
                "            cursor: grabbing;\n" +
                "        }\n" +
                "        .mermaid {\n" +
                "            width: 100%;\n" +
                "            transition: transform 0.05s ease-out;\n" +
                "            transform-origin: center center;\n" +
                "        }\n" +
                "        /* Controles de Zoom Premium */\n" +
                "        .zoom-controls {\n" +
                "            position: absolute;\n" +
                "            bottom: 1rem;\n" +
                "            right: 1rem;\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            gap: 0.5rem;\n" +
                "            z-index: 10;\n" +
                "        }\n" +
                "        .zoom-controls button {\n" +
                "            background: rgba(255, 255, 255, 0.9);\n" +
                "            border: 0.5px solid var(--border);\n" +
                "            color: var(--text);\n" +
                "            font-size: 1.1rem;\n" +
                "            font-weight: 600;\n" +
                "            width: 36px;\n" +
                "            height: 36px;\n" +
                "            border-radius: 0px;\n" +
                "            cursor: pointer;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);\n" +
                "            transition: all 0.2s;\n" +
                "            backdrop-filter: blur(8px);\n" +
                "        }\n" +
                "        .zoom-controls button:hover {\n" +
                "            background: var(--primary);\n" +
                "            color: white;\n" +
                "            border-color: var(--primary);\n" +
                "            transform: translateY(-1px);\n" +
                "        }\n" +
                "        .zoom-controls button:active {\n" +
                "            transform: translateY(0);\n" +
                "        }\n" +
                "        /* Lado del Código */\n" +
                "        .code-body {\n" +
                "            background: var(--code-bg);\n" +
                "            margin: 0;\n" +
                "            font-family: 'Fira Code', monospace;\n" +
                "            font-size: 0.9rem;\n" +
                "            line-height: 1.5;\n" +
                "            color: #0f172a;\n" +
                "            white-space: pre-wrap;\n" +
                "        }\n" +
                "        .btn-copy {\n" +
                "            background: var(--primary);\n" +
                "            color: white;\n" +
                "            border: none;\n" +
                "            padding: 0.4rem 0.8rem;\n" +
                "            border-radius: 0px;\n" +
                "            font-family: inherit;\n" +
                "            font-size: 0.85rem;\n" +
                "            font-weight: 600;\n" +
                "            cursor: pointer;\n" +
                "            transition: all 0.2s;\n" +
                "        }\n" +
                "        .btn-copy:hover {\n" +
                "            background: var(--primary-hover);\n" +
                "            transform: translateY(-1px);\n" +
                "        }\n" +
                "        .btn-copy:active {\n" +
                "            transform: translateY(0);\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 2rem;\n" +
                "            text-align: center;\n" +
                "            font-size: 0.95rem;\n" +
                "            color: var(--text-secondary);\n" +
                "            border-top: 0.5px solid var(--border);\n" +
                "            padding-top: 1.5rem;\n" +
                "        }\n" +
                "        .footer strong {\n" +
                "            color: var(--text);\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <header>\n" +
                "            <h1>Diagrama de Clases UML Interactivo</h1>\n" +
                "            <div class=\"subtitle\">Traductor Automático de Java a Mermaid JS</div>\n" +
                "        </header>\n" +
                "        \n" +
                "        <div class=\"content-grid\">\n" +
                "            <!-- Panel Izquierdo: UML -->\n" +
                "            <div class=\"panel\">\n" +
                "                <div class=\"panel-header\">\n" +
                "                    <h2 class=\"panel-title\">\n" +
                "                        <span>Vista del Diagrama UML</span>\n" +
                "                    </h2>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body uml-body\">\n" +
                "                    <pre class=\"mermaid\">" + codigoMermaidEscapado.trim() + "</pre>\n" +
                "                    <div class=\"zoom-controls\">\n" +
                "                        <button onclick=\"zoomIn()\" title=\"Acercar\">＋</button>\n" +
                "                        <button onclick=\"zoomOut()\" title=\"Alejar\">－</button>\n" +
                "                        <button onclick=\"resetZoom()\" title=\"Restablecer\">⟲</button>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <!-- Panel Derecho: Código -->\n" +
                "            <div class=\"panel\">\n" +
                "                <div class=\"panel-header\">\n" +
                "                    <h2 class=\"panel-title\">\n" +
                "                        <span>Código Mermaid JS</span>\n" +
                "                    </h2>\n" +
                "                    <button class=\"btn-copy\" onclick=\"copyCode()\">Copiar Código</button>\n" +
                "                </div>\n" +
                "                <pre class=\"panel-body code-body\" id=\"mermaid-code\">" + codigoMermaidEscapado + "</pre>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"footer\">\n" +
                "            <p>Archivo de origen: <code>" + origen + "</code></p>\n" +
                "            <p>Desarrollado por: <strong>Deraz Labrador Emmanuel</strong> y <strong>Salazar Leal Javier Issac</strong></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script type=\"module\">\n" +
                "        import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';\n" +
                "        mermaid.initialize({\n" +
                "            startOnLoad: true,\n" +
                "            theme: 'default',\n" +
                "            securityLevel: 'loose'\n" +
                "        });\n" +
                "    </script>\n" +
                "    <script>\n" +
                "        // Zoom & Pan interactivo para el Diagrama UML\n" +
                "        let scale = 1;\n" +
                "        let pointX = 0;\n" +
                "        let pointY = 0;\n" +
                "        let startX = 0;\n" +
                "        let startY = 0;\n" +
                "        let isDragging = false;\n" +
                "\n" +
                "        const container = document.querySelector('.uml-body');\n" +
                "\n" +
                "        // Configuración inicial del SVG cuando se renderiza\n" +
                "        const observer = new MutationObserver((mutations) => {\n" +
                "            const svg = container.querySelector('svg');\n" +
                "            if (svg) {\n" +
                "                svg.style.transition = 'transform 0.05s ease-out';\n" +
                "                svg.style.transformOrigin = 'center center';\n" +
                "                observer.disconnect();\n" +
                "            }\n" +
                "        });\n" +
                "        observer.observe(container, { childList: true, subtree: true });\n" +
                "\n" +
                "        container.addEventListener('mousedown', (e) => {\n" +
                "            if (e.target.closest('.zoom-controls')) return;\n" +
                "            e.preventDefault();\n" +
                "            const svg = container.querySelector('svg');\n" +
                "            if (!svg) return;\n" +
                "            isDragging = true;\n" +
                "            startX = e.clientX - pointX;\n" +
                "            startY = e.clientY - pointY;\n" +
                "        });\n" +
                "\n" +
                "        window.addEventListener('mouseup', () => {\n" +
                "            isDragging = false;\n" +
                "        });\n" +
                "\n" +
                "        container.addEventListener('mousemove', (e) => {\n" +
                "            if (!isDragging) return;\n" +
                "            const svg = container.querySelector('svg');\n" +
                "            if (!svg) return;\n" +
                "            pointX = e.clientX - startX;\n" +
                "            pointY = e.clientY - startY;\n" +
                "            updateTransform(svg);\n" +
                "        });\n" +
                "\n" +
                "        container.addEventListener('wheel', (e) => {\n" +
                "            e.preventDefault();\n" +
                "            const svg = container.querySelector('svg');\n" +
                "            if (!svg) return;\n" +
                "            \n" +
                "            const zoomSpeed = 0.08;\n" +
                "            if (e.deltaY < 0) {\n" +
                "                scale = Math.min(scale + zoomSpeed, 5);\n" +
                "            } else {\n" +
                "                scale = Math.max(scale - zoomSpeed, 0.15);\n" +
                "            }\n" +
                "            updateTransform(svg);\n" +
                "        });\n" +
                "\n" +
                "        function updateTransform(svg) {\n" +
                "            svg.style.transform = `translate(${pointX}px, ${pointY}px) scale(${scale})`;\n" +
                "        }\n" +
                "\n" +
                "        window.zoomIn = function() {\n" +
                "            const svg = container.querySelector('svg');\n" +
                "            if (!svg) return;\n" +
                "            scale = Math.min(scale + 0.15, 5);\n" +
                "            updateTransform(svg);\n" +
                "        }\n" +
                "\n" +
                "        window.zoomOut = function() {\n" +
                "            const svg = container.querySelector('svg');\n" +
                "            if (!svg) return;\n" +
                "            scale = Math.max(scale - 0.15, 0.15);\n" +
                "            updateTransform(svg);\n" +
                "        }\n" +
                "\n" +
                "        window.resetZoom = function() {\n" +
                "            const svg = container.querySelector('svg');\n" +
                "            if (!svg) return;\n" +
                "            scale = 1;\n" +
                "            pointX = 0;\n" +
                "            pointY = 0;\n" +
                "            updateTransform(svg);\n" +
                "        }\n" +
                "\n" +
                "        window.copyCode = function() {\n" +
                "            const codeText = document.getElementById('mermaid-code').innerText;\n" +
                "            navigator.clipboard.writeText(codeText).then(() => {\n" +
                "                const btn = document.querySelector('.btn-copy');\n" +
                "                const originalText = btn.innerText;\n" +
                "                btn.innerText = '¡Copiado!';\n" +
                "                btn.style.background = '#10b981';\n" +
                "                setTimeout(() => {\n" +
                "                    btn.innerText = originalText;\n" +
                "                    btn.style.background = '';\n" +
                "                }, 2000);\n" +
                "            }).catch(err => {\n" +
                "                console.error('Error al copiar el código: ', err);\n" +
                "            });\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        try (FileWriter writer = new FileWriter(rutaSalida)) {
            writer.write(htmlTemplate);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo " + rutaSalida + ": " + e.getMessage());
        }
    }
}
