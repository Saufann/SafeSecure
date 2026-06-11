# -*- coding: utf-8 -*-
"""
Convert Laporan Markdown to PDF using fpdf2 with markdown2pdf support.
"""
import re
import os

from fpdf import FPDF


class LaporanPDF(FPDF):
    """Custom PDF class for the report."""

    def __init__(self):
        super().__init__(orientation="P", unit="mm", format="A4")
        self.set_auto_page_break(auto=True, margin=25)
        # Use built-in fonts that support basic latin characters
        self.add_page()

    def header(self):
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(150, 150, 150)
        self.cell(
            0,
            8,
            "Laporan Tugas Besar Kriptografi - Evaluasi Keamanan Kata Sandi",
            align="C",
        )
        self.ln(4)
        # Draw a line
        self.set_draw_color(200, 200, 200)
        self.line(10, self.get_y(), 200, self.get_y())
        self.ln(6)

    def footer(self):
        self.set_y(-15)
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(150, 150, 150)
        self.cell(0, 10, f"Halaman {self.page_no()}/{{nb}}", align="C")

    def write_title(self, text):
        self.set_font("Helvetica", "B", 18)
        self.set_text_color(25, 25, 112)
        self.multi_cell(0, 10, text, align="C")
        self.ln(3)

    def write_subtitle(self, text):
        self.set_font("Helvetica", "B", 14)
        self.set_text_color(25, 25, 112)
        self.multi_cell(0, 9, text, align="C")
        self.ln(2)

    def write_h2(self, text):
        self.ln(4)
        self.set_font("Helvetica", "B", 14)
        self.set_text_color(30, 60, 120)
        self.multi_cell(0, 9, text)
        # underline
        self.set_draw_color(30, 60, 120)
        self.line(10, self.get_y(), 200, self.get_y())
        self.ln(4)

    def write_h3(self, text):
        self.ln(3)
        self.set_font("Helvetica", "B", 12)
        self.set_text_color(40, 80, 140)
        self.multi_cell(0, 8, text)
        self.ln(2)

    def write_h4(self, text):
        self.ln(2)
        self.set_font("Helvetica", "B", 10)
        self.set_text_color(60, 60, 60)
        self.multi_cell(0, 7, text)
        self.ln(1)

    def write_paragraph(self, text):
        self.set_font("Helvetica", "", 10)
        self.set_text_color(30, 30, 30)
        # Clean markdown formatting
        text = self._clean_markdown(text)
        self.multi_cell(0, 6, text)
        self.ln(2)

    def write_bullet(self, text, indent=0):
        self.set_font("Helvetica", "", 10)
        self.set_text_color(30, 30, 30)
        text = self._clean_markdown(text)
        x = 15 + indent * 5
        self.set_x(x)
        bullet = "- "  # simple dash bullet for latin-1 compat
        self.multi_cell(0, 6, bullet + text)
        self.ln(1)

    def write_numbered(self, number, text, indent=0):
        self.set_font("Helvetica", "", 10)
        self.set_text_color(30, 30, 30)
        text = self._clean_markdown(text)
        x = 15 + indent * 5
        self.set_x(x)
        self.multi_cell(0, 6, f"{number}. {text}")
        self.ln(1)

    def write_code_block(self, text):
        self.set_font("Courier", "", 9)
        self.set_text_color(50, 50, 50)
        self.set_fill_color(240, 240, 245)
        # Add padding
        x = self.get_x()
        self.set_x(15)
        text = self._clean_markdown(text)
        lines = text.split("\n")
        for line in lines:
            if self.get_y() > 270:
                self.add_page()
            self.set_x(15)
            # Truncate long lines
            if len(line) > 90:
                line = line[:87] + "..."
            self.cell(180, 5, line, fill=True)
            self.ln()
        self.ln(3)

    def write_table(self, headers, rows):
        self.set_font("Helvetica", "B", 9)
        self.set_text_color(255, 255, 255)
        self.set_fill_color(40, 80, 140)

        # Calculate column widths
        num_cols = len(headers)
        available_width = 185
        col_widths = [available_width // num_cols] * num_cols
        # Give extra space to last column
        col_widths[-1] = available_width - sum(col_widths[:-1])

        row_height = 7

        # Headers
        self.set_x(12)
        for i, header in enumerate(headers):
            header = self._clean_markdown(header.strip())
            self.cell(col_widths[i], row_height, header, border=1, fill=True, align="C")
        self.ln()

        # Rows
        self.set_font("Helvetica", "", 8)
        self.set_text_color(30, 30, 30)
        fill = False
        for row in rows:
            if self.get_y() > 265:
                self.add_page()
            self.set_x(12)
            if fill:
                self.set_fill_color(245, 245, 250)
            else:
                self.set_fill_color(255, 255, 255)

            # Pad or truncate row to match num_cols
            padded_row = list(row)
            while len(padded_row) < num_cols:
                padded_row.append("")
            padded_row = padded_row[:num_cols]

            # Calculate max height needed
            max_lines = 1
            cell_texts = []
            for i, cell in enumerate(padded_row):
                cell_text = self._clean_markdown(cell.strip())
                cell_texts.append(cell_text)
                cw = max(col_widths[i] // 2, 1)
                lines_needed = max(1, len(cell_text) // cw + 1)
                max_lines = max(max_lines, lines_needed)

            actual_height = row_height * max_lines

            for i, cell_text in enumerate(cell_texts):
                x_before = self.get_x()
                y_before = self.get_y()
                # Use multi_cell for wrapping
                self.multi_cell(
                    col_widths[i], row_height, cell_text, border=1, fill=True
                )
                # Move to next column position
                self.set_xy(x_before + col_widths[i], y_before)

            self.ln(actual_height)
            fill = not fill
        self.ln(3)

    def write_separator(self):
        self.ln(2)
        self.set_draw_color(180, 180, 180)
        y = self.get_y()
        self.line(10, y, 200, y)
        self.ln(4)

    def _clean_markdown(self, text):
        """Remove markdown formatting from text."""
        # Remove bold
        text = re.sub(r"\*\*(.+?)\*\*", r"\1", text)
        # Remove italic
        text = re.sub(r"\*(.+?)\*", r"\1", text)
        # Remove inline code
        text = re.sub(r"`(.+?)`", r"\1", text)
        # Remove links, keep text
        text = re.sub(r"\[(.+?)\]\(.+?\)", r"\1", text)
        # Clean special chars that fpdf can't handle with latin-1 encoding
        replacements = {
            "\u2014": "-",   # em dash
            "\u2013": "-",   # en dash
            "\u2018": "'",   # left single quote
            "\u2019": "'",   # right single quote
            "\u201c": '"',   # left double quote
            "\u201d": '"',   # right double quote
            "\u2022": "-",   # bullet
            "\u2265": ">=",  # >=
            "\u2264": "<=",  # <=
            "\u00d7": "x",   # multiplication sign
            "\u2192": "->",  # right arrow
            "\u2190": "<-",  # left arrow
            "\u2026": "...", # ellipsis
            "\u00b2": "2",   # superscript 2
            "\u2079": "9",   # superscript 9
            "\u2070": "0",   # superscript 0
            "\u00b9": "1",   # superscript 1
            "\u25bc": "v",   # down triangle
            "\u25b6": ">",   # right triangle
            "\u2500": "-",   # box drawing
            "\u2502": "|",   # box drawing vertical
            "\u250c": "+",   # box drawing corner
            "\u2510": "+",
            "\u2514": "+",
            "\u2518": "+",
            "\u251c": "+",
            "\u2524": "+",
            "\u252c": "+",
            "\u2534": "+",
            "\u253c": "+",
            "\u2588": "#",   # full block
            "\u2580": "=",   # upper half block
            "\u2584": "=",   # lower half block
            "\u25a0": "#",   # black square
            "\u25a1": "[]",  # white square
            "\u2248": "~=",  # approximately
            "\u2260": "!=",  # not equal
            "\u221e": "inf", # infinity
            "\u2211": "SUM", # summation
        }
        for old, new in replacements.items():
            text = text.replace(old, new)
        # Remove any remaining non-latin1 characters
        text = text.encode("latin-1", errors="replace").decode("latin-1")
        return text


def parse_markdown(filepath):
    """Parse the markdown file and return structured content."""
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    lines = content.split("\n")
    elements = []
    i = 0
    in_code_block = False
    code_buffer = []
    in_table = False
    table_headers = []
    table_rows = []

    while i < len(lines):
        line = lines[i]
        stripped = line.strip()

        # Code block toggle
        if stripped.startswith("```"):
            if in_code_block:
                elements.append(("code", "\n".join(code_buffer)))
                code_buffer = []
                in_code_block = False
            else:
                # Flush table if active
                if in_table:
                    elements.append(("table", (table_headers, table_rows)))
                    table_headers = []
                    table_rows = []
                    in_table = False
                in_code_block = True
            i += 1
            continue

        if in_code_block:
            code_buffer.append(line)
            i += 1
            continue

        # Table detection
        if "|" in stripped and stripped.startswith("|") and stripped.endswith("|"):
            cells = [c.strip() for c in stripped.split("|")[1:-1]]
            # Check if separator row
            if all(re.match(r"^[-:]+$", c) for c in cells):
                i += 1
                continue
            if not in_table:
                in_table = True
                table_headers = cells
            else:
                table_rows.append(cells)
            i += 1
            continue
        else:
            if in_table:
                elements.append(("table", (table_headers, table_rows)))
                table_headers = []
                table_rows = []
                in_table = False

        # Headers
        if stripped.startswith("# ") and not stripped.startswith("## "):
            elements.append(("h1", stripped[2:].strip()))
        elif stripped.startswith("## "):
            elements.append(("h2", stripped[3:].strip()))
        elif stripped.startswith("### "):
            elements.append(("h3", stripped[4:].strip()))
        elif stripped.startswith("#### "):
            elements.append(("h4", stripped[5:].strip()))
        elif stripped.startswith("---"):
            elements.append(("separator", ""))
        elif re.match(r"^\d+\.\s", stripped):
            match = re.match(r"^(\d+)\.\s+(.*)", stripped)
            if match:
                elements.append(("numbered", (match.group(1), match.group(2))))
        elif stripped.startswith("- "):
            indent = (len(line) - len(line.lstrip())) // 2
            elements.append(("bullet", (stripped[2:], indent)))
        elif stripped == "":
            pass  # skip empty lines
        else:
            elements.append(("paragraph", stripped))

        i += 1

    # Flush remaining table
    if in_table:
        elements.append(("table", (table_headers, table_rows)))

    return elements


def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    md_file = os.path.join(script_dir, "Laporan_Evaluasi_Keamanan_Kata_Sandi.md")
    pdf_file = os.path.join(script_dir, "Laporan_Evaluasi_Keamanan_Kata_Sandi.pdf")

    print(f"Parsing: {md_file}")
    elements = parse_markdown(md_file)

    print("Generating PDF...")
    pdf = LaporanPDF()
    pdf.alias_nb_pages()

    first_h1 = True
    for elem_type, content in elements:
        # Check if we need a new page
        if pdf.get_y() > 270:
            pdf.add_page()

        if elem_type == "h1":
            if first_h1:
                pdf.write_title(content)
                first_h1 = False
            else:
                pdf.write_subtitle(content)
        elif elem_type == "h2":
            pdf.write_h2(content)
        elif elem_type == "h3":
            pdf.write_h3(content)
        elif elem_type == "h4":
            pdf.write_h4(content)
        elif elem_type == "paragraph":
            pdf.write_paragraph(content)
        elif elem_type == "bullet":
            text, indent = content
            pdf.write_bullet(text, indent)
        elif elem_type == "numbered":
            num, text = content
            pdf.write_numbered(num, text)
        elif elem_type == "code":
            pdf.write_code_block(content)
        elif elem_type == "table":
            headers, rows = content
            if headers and rows:
                pdf.write_table(headers, rows)
        elif elem_type == "separator":
            pdf.write_separator()

    pdf.output(pdf_file)
    print(f"PDF berhasil dibuat: {pdf_file}")
    print(f"Jumlah halaman: {pdf.pages_count}")


if __name__ == "__main__":
    main()
