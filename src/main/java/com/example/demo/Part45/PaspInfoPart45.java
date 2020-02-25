package com.example.demo.Part45;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PaspInfoPart45 implements GetInfoPart2,ConvertString{
	private int nameElem;
	private int eThikness;
	private int eSteel;
	private int eGost;
	private int rowIndex;
	private String paspPart;
	private Workbook wb;
	private String fileName;
	private double desTemp;
	private double desPress;

	public PaspInfoPart45(Workbook wb,String paspPart,String fileName) {
		this.wb=wb;
		this.paspPart=paspPart;
		this.fileName=fileName;
		this.desPress=this.getMaxValue(this.GetPressTemp(wb, 0).toString());
		this.desTemp=this.getMaxValue(this.GetPressTemp(wb, 1).toString());
	}


	private boolean PasreCellValue(Cell cell) {
		try {
			cell.getStringCellValue();
			return true;
		} catch(IllegalStateException e) {
			return false;
		}

	}
	private void GetInfoPart51() {
		Sheet iSheet=this.wb.getSheet(this.paspPart);
		Iterator<Row> rIter=iSheet.rowIterator();
		while(rIter.hasNext()) {
			Iterator<Cell> cIter=rIter.next().cellIterator();
			while(cIter.hasNext()) {
				Cell cell=cIter.next();

				//	System.out.println("row "+cell.getRowIndex()+ " column "+cell.getColumnIndex());
				if(cell!=null&&cell.toString().toLowerCase().contains("наименован")&&
						cell.getStringCellValue().toLowerCase().contains("элемент")) {
					this.nameElem=cell.getColumnIndex();
					this.rowIndex=cell.getRowIndex();
				}

				if(cell!=null&&cell.toString().toLowerCase().contains("наружн")&&
						cell.toString().toLowerCase().contains("диамет")&&cell.toString().toLowerCase().contains("стенк")) {
					this.eThikness=cell.getColumnIndex();
				}

				if(cell!=null&&cell.toString().toLowerCase().contains("марк")
						&&cell.getStringCellValue().toLowerCase().contains("стали")) {
					this.eSteel=cell.getColumnIndex();
				}

				if(cell!=null&&cell.toString().toLowerCase().contains("гост")&&
						cell.getStringCellValue().toLowerCase().contains("ту")) {
					this.eGost=cell.getColumnIndex();
				}
			}
		}
	}

	public void FillTable(List<NewRow<String>> resultList) {
		this.GetInfoPart51();
		Sheet sht=this.wb.getSheet(paspPart);
		int[] mass={this.nameElem,this.eThikness,this.eSteel,this.eGost};
		for (int i=this.rowIndex+1;i<sht.getLastRowNum()+1;i++) {
			try {
				if (sht.getRow(i)!=null &&
						sht.getRow(i).getCell(this.nameElem)!=null &&
						sht.getRow(i).getCell(this.nameElem).getCellType()!=CellType.BLANK) {
					NewRow<String> row=new NewRow<>();
					row.addValue(0,this.fileName);
					for(int j=0;j<mass.length;j++) {
						String str=sht.getRow(i).getCell(mass[j]).toString();
						row.addValue(str);
					}
					row.addValue(Double.toString(this.desPress).replace(".", ","));
					row.addValue(Double.toString(this.desTemp).replace(".", ","));
					resultList.add(row);
				}
			}catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
	}




}