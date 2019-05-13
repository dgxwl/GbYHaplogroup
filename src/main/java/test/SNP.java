package test;

import java.io.Serializable;

public class SNP implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String haplogroup;
	private String rsid;
	private String hg19Pos;
	private String hg38Pos;
	private String original;
	private String mutant;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHaplogroup() {
		return haplogroup;
	}

	public void setHaplogroup(String haplogroup) {
		this.haplogroup = haplogroup;
	}

	public String getHg19Pos() {
		return hg19Pos;
	}

	public String getRsid() {
		return rsid;
	}

	public void setRsid(String rsid) {
		this.rsid = rsid;
	}

	public void setHg19Pos(String hg19Pos) {
		this.hg19Pos = hg19Pos;
	}

	public String getHg38Pos() {
		return hg38Pos;
	}

	public void setHg38Pos(String hg38Pos) {
		this.hg38Pos = hg38Pos;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getMutant() {
		return mutant;
	}

	public void setMutant(String mutant) {
		this.mutant = mutant;
	}

	@Override
	public String toString() {
		return "SNP [name=" + name + ", haplogroup=" + haplogroup + ", rsid=" + rsid + ", hg19Pos=" + hg19Pos
				+ ", hg38Pos=" + hg38Pos + ", original=" + original + ", mutant=" + mutant + "]";
	}

}
