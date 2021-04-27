package io.luchta.forma4j.reader.excel.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ProcessInfoCollection implements Iterable<ProcessInfo>, Serializable {

	private static final long serialVersionUID = 1L;
    
    private List<ProcessInfo> list = new ArrayList<>();
    
    public ProcessInfoCollection() {}
    
    public ProcessInfoCollection(List<ProcessInfo> list) {
        this.list = list;
    }
    
    public int size() {
        return this.list.size();
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public void add(ProcessInfo processInfo) {
        this.list.add(processInfo);
    }
    
    public ProcessInfo get(int index) {
        return this.list.get(index);
    }
    
    public int latestRowNumber() {

        Optional<ProcessInfo> optional = this.list.stream().max((o1, o2) -> o1.currentRowNumber().compareTo(o2.currentRowNumber()));
    	
    	if (optional.isEmpty()) {
    		return 0;
    	}
    	
    	return optional.get().currentRowNumber().intValue();
    }
    
    public int latestColNumber() {

    	Optional<ProcessInfo> optional = this.list.stream().max((o1, o2) -> o1.currentColNumber().compareTo(o2.currentColNumber()));
    	
    	if (optional.isEmpty()) {
    		return 0;
    	}
    	
    	return optional.get().currentColNumber().intValue();
    }

    @Override
    public Iterator<ProcessInfo> iterator() {
        return this.list.iterator();
    }
}
