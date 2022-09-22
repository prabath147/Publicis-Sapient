import { faArrowDown, faArrowsUpDown, faArrowUp } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ActionIcon } from "@mantine/core";

export default function SortableHeader(props) {

    const onClicked = () => {
        const toggle = props.toggle;
        for(const [key,] of Object.entries(toggle)){
            if(key === props.label)
                toggle[props.label] = (toggle[props.label] + 1)%3;
            else
                toggle[key] = 0;
        }
        props.setToggle({...toggle});
        
    }

    return (
        <th>
            <div style={{display:'flex', alignItems: 'center', flexDirection:'row-reverse'}}>
            <ActionIcon onClick={onClicked}>
                <FontAwesomeIcon icon={props.toggle[props.label] === 0 ? faArrowsUpDown: props.toggle[props.label] === 1? faArrowUp: faArrowDown} />
            </ActionIcon>
            <p>{props.label}</p>
            </div>
        </th>
    )
}